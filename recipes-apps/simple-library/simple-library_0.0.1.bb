DESCRIPTION = "Example library use"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "nlohmann-json gtest spdlog"

inherit cmake

SRC_URI = "git://github.com/ancor1369/simple-library.git;branch=main;protocol=https"
SRCREV = "bb33a4d1b3f9a7777c0fa39041bc7f359af29cf1"

S = "${WORKDIR}/git"

do_install () {
    
}

FILES:${PN} += "${bindir}/simple-library-test"
