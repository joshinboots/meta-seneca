SUMMARY = "Tiny GTK3 dashboard that reads /tmp/pir_fifo and shows MOTION/IDLE"
HOMEPAGE = "https://github.com/joshinboots/sensor-dashboard"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "1.0+git${SRCPV}"

# Pull your GUI repo
SRC_URI = "git://github.com/joshinboots/sensor-dashboard.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Build deps and pkg-config for the Makefile
DEPENDS = "gtk+3"
inherit pkgconfig

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 sensor-dashboard ${D}${bindir}/
}

FILES:${PN} += "${bindir}/sensor-dashboard"
