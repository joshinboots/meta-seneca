DESCRIPTION = "Simple library application with test binary"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4e2e0c74875dc559f04c1b46aebbd82c"

SRC_URI = "git://github.com/LNX500-summer-2025/Lab-Test.git;protocol=https;branch=LAB_TEST"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit pkgconfig

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 build/simple-library ${D}${bindir}/simple-library
    install -m 0755 build/simple-library-test ${D}${bindir}/simple-library-test
}
