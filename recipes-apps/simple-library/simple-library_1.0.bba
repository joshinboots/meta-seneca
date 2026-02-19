SUMMARY = "Simple-Library application and unit test"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/LNX500-summer-2025/simple-library.git;protocol=https;branch=LAB_TEST"
SRCREV  = "${AUTOREV}"

S = "${WORKDIR}/git"

# GoogleTest is required for the test binary
DEPENDS = "gtest"

inherit pkgconfig

# -------- Build using our Makefile --------
do_compile() {
    oe_runmake
}

# -------- Install the two binaries --------
do_install() {
    install -d ${D}${bindir}
    install -m 0755 build/simple-library        ${D}${bindir}
    install -m 0755 build/simple-library-test   ${D}${bindir}
}

# What the main package provides
FILES:${PN} += "${bindir}/simple-library ${bindir}/simple-library-test"
