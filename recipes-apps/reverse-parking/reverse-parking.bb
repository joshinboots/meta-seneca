SUMMARY = "Reverse Parking meter (HC-SR04 + buzzer) with HDMI display"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
  file://CMakeLists.txt \
  file://main.cpp \
  file://reverse-parking-launch.sh \
  file://reverse-parking.conf \
"

S = "${WORKDIR}"

inherit cmake pkgconfig

DEPENDS = "gtkmm3 libgpiod"

do_install() {
    install -d ${D}${bindir} ${D}${sysconfdir}
    install -m 0755 reverse-parking ${D}${bindir}/
    install -m 0755 ${WORKDIR}/reverse-parking-launch.sh ${D}${bindir}/
    install -m 0644 ${WORKDIR}/reverse-parking.conf ${D}${sysconfdir}/
}

FILES:${PN} += " \
  ${bindir}/reverse-parking \
  ${bindir}/reverse-parking-launch.sh \
  ${sysconfdir}/reverse-parking.conf \
"
