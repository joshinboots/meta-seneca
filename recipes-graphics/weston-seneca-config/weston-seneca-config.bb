SUMMARY = "Weston config for BeaglePlay HDMI (Wayland + Xwayland)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Our weston.ini is in "files/"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://weston.ini"

S = "${WORKDIR}"

RDEPENDS:${PN} += "weston"

do_install() {
    install -d ${D}${sysconfdir}/xdg/weston
    install -m 0644 ${WORKDIR}/weston.ini ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
