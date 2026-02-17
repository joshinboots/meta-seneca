DESCRIPTION = "Example library use"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "nlohmann-json gtest spdlog"

SRC_URI = "git://github.com/rashikrahmanishmum/simple-library.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/../workspace/sources/simple-library"

inherit cmake

EXTRA_OECMAKE = "."

do_install () {
    install -d ${D}${bindir}
    install -m 0755 tests/simple-library-test ${D}${bindir}/
}

FILES:${PN} += "${bindir}/simple-library-test"

