#include <gtkmm.h>
#include <glibmm/main.h>
#include <gpiod.h>

#include <atomic>
#include <chrono>
#include <cmath>
#include <csignal>
#include <fstream>
#include <sstream>
#include <string>
#include <thread>

using namespace std::chrono;

static std::atomic<bool>  g_running{true};
static std::atomic<float> g_last_distance_cm{-1.f};
static std::atomic<int>   g_beep_period_ms{0};  // 0 => mute
static const int BEEP_ON_MS = 150;

// Simple GPIO configuration loaded from /etc/reverse-parking.conf
struct GpioCfg {
    int chip = 0;     // /dev/gpiochipN
    int trig = 23;    // example pins; update to your wiring
    int echo = 24;
    int buzz = 25;
};

static GpioCfg load_cfg(const std::string& path) {
    GpioCfg cfg{};
    std::ifstream f(path);
    std::string line;
    while (std::getline(f, line)) {
        std::istringstream is(line);
        std::string k, eq;
        int v;
        if (is >> k >> eq >> v && eq == "=") {
            if (k == "chip") cfg.chip = v;
            else if (k == "trig") cfg.trig = v;
            else if (k == "echo") cfg.echo = v;
            else if (k == "buzz") cfg.buzz = v;
        }
    }
    return cfg;
}

static bool wait_level(struct gpiod_line* ln, int level, int timeout_us) {
    auto t0 = steady_clock::now();
    while (g_running.load()) {
        int val = gpiod_line_get_value(ln);
        if (val < 0) return false;
        if (val == level) return true;
        if (duration_cast<microseconds>(steady_clock::now() - t0).count() >= timeout_us)
            return false;
        std::this_thread::sleep_for(microseconds(10));
    }
    return false;
}

static bool measure_cm(struct gpiod_line* trig, struct gpiod_line* echo, float& out_cm) {
    // 10us trigger pulse
    gpiod_line_set_value(trig, 0);
    std::this_thread::sleep_for(microseconds(2));
    gpiod_line_set_value(trig, 1);
    std::this_thread::sleep_for(microseconds(10));
    gpiod_line_set_value(trig, 0);

    if (!wait_level(echo, 1, 25000))  // wait up to 25 ms for rising edge
        return false;
    auto t1 = steady_clock::now();

    if (!wait_level(echo, 0, 35000))  // then wait up to 35 ms to fall
        return false;
    auto t2 = steady_clock::now();

    auto usec = duration_cast<microseconds>(t2 - t1).count();
    // HC-SR04 ≈ 58us per cm (rough & classic)
    out_cm = static_cast<float>(usec) / 58.0f;
    return (out_cm > 0.f && out_cm < 400.f);
}

static void sensor_thread(GpioCfg cfg) {
    auto* chip = gpiod_chip_open_by_number(cfg.chip);
    if (!chip) { g_running = false; return; }

    auto* trig = gpiod_chip_get_line(chip, cfg.trig);
    auto* echo = gpiod_chip_get_line(chip, cfg.echo);

    if (!trig || !echo ||
        gpiod_line_request_output(trig, "reverse-parking", 0) ||
        gpiod_line_request_input (echo, "reverse-parking")) {
        gpiod_chip_close(chip);
        g_running = false;
        return;
    }

    while (g_running.load()) {
        float cm = -1.f;
        if (measure_cm(trig, echo, cm)) {
            g_last_distance_cm.store(cm);

            // Map distance to beep rates:
            // <=10cm -> 30 bpm (2000ms period)
            // <=20cm -> 15 bpm (4000ms period)
            // else   -> mute
            int period = 0;
            if (cm > 0 && cm <= 10.f)      period = 2000;
            else if (cm <= 20.f)           period = 4000;
            else                            period = 0;
            g_beep_period_ms.store(period);
        } else {
            // No echo -> mute and mark unknown
            g_last_distance_cm.store(-1.f);
            g_beep_period_ms.store(0);
        }
        std::this_thread::sleep_for(milliseconds(50));
    }

    gpiod_line_release(trig);
    gpiod_line_release(echo);
    gpiod_chip_close(chip);
}

static void buzzer_thread(GpioCfg cfg) {
    auto* chip = gpiod_chip_open_by_number(cfg.chip);
    if (!chip) { g_running = false; return; }

    auto* buzz = gpiod_chip_get_line(chip, cfg.buzz);
    if (!buzz || gpiod_line_request_output(buzz, "reverse-parking", 0)) {
        gpiod_chip_close(chip); g_running=false; return;
    }

    while (g_running.load()) {
        int period = g_beep_period_ms.load();
        if (period <= 0) {
            gpiod_line_set_value(buzz, 0);
            std::this_thread::sleep_for(milliseconds(50));
            continue;
        }
        gpiod_line_set_value(buzz, 1);
        std::this_thread::sleep_for(milliseconds(BEEP_ON_MS));
        gpiod_line_set_value(buzz, 0);
        auto off_ms = period - BEEP_ON_MS;
        if (off_ms < 0) off_ms = 0;
        std::this_thread::sleep_for(milliseconds(off_ms));
    }

    gpiod_line_release(buzz);
    gpiod_chip_close(chip);
}

int main(int argc, char** argv) {
    // Load GPIO config
    GpioCfg cfg = load_cfg("/etc/reverse-parking.conf");

    // Start worker threads
    std::thread t_sensor(sensor_thread, cfg);
    std::thread t_buzz  (buzzer_thread, cfg);

    // GTK UI
    auto app = Gtk::Application::create("ca.seneca.reverseparking");
    Gtk::Window win;
    win.set_title("Reverse Parking");
    win.set_default_size(640, 360);

    Gtk::Box vbox(Gtk::ORIENTATION_VERTICAL, 8);
    vbox.set_margin_top(20);
    vbox.set_margin_bottom(20);
    vbox.set_margin_start(20);
    vbox.set_margin_end(20);

    Gtk::Label title("Distance");
    title.set_halign(Gtk::ALIGN_CENTER);
    title.set_margin_bottom(12);
    title.set_hexpand(true);

    Gtk::Label value("--.- cm");
    value.set_halign(Gtk::ALIGN_CENTER);
    value.set_hexpand(true);
    value.override_font(Pango::FontDescription("Sans 48"));

    vbox.pack_start(title, Gtk::PACK_SHRINK);
    vbox.pack_start(value, Gtk::PACK_EXPAND_WIDGET);
    win.add(vbox);
    vbox.show_all();

    // UI timer to refresh label every 100ms
    auto conn = Glib::signal_timeout().connect([&]() -> bool {
        float cm = g_last_distance_cm.load();
        if (cm < 0) value.set_text("--.- cm");
        else {
            char buf[32];
            std::snprintf(buf, sizeof(buf), "%.1f cm", cm);
            value.set_text(buf);
        }
        return g_running.load(); // keep ticking while running
    }, 100);

    // Stop cleanly on SIGINT from serial/ssh
    std::signal(SIGINT, [](int){ g_running = false; });

    int rc = app->run(win);
    g_running = false;

    if (t_sensor.joinable()) t_sensor.join();
    if (t_buzz.joinable())   t_buzz.join();
    return rc;
}
