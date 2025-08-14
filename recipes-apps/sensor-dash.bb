DESCRIPTION = "Sensor Dashboard example recipe"

LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


DEPENDS =  "nlohmann-json \

            spdlog \

            wayland \

            mesa-pvr \

            gtkmm3  \

            cairo \

            "



inherit cmake


do_configure[depends] += "pkgconfig-native:do_populate_sysroot"


SRC_URI = "git://github.com/biaxdev/gui-sample;branch=ac/remove_sdl2;protocol=https"

SRCREV = "8bed6f48690bf1e2035aa712e4a4e817bf2ff559"                    

S = "${WORKDIR}/git"

do_install() {

    install -d ${D}${bindir}

    install -m 0755 sensor-dashboard ${D}${bindir}/

}


FILES:${PN} += "${bindir}/sensor-dashboard"
