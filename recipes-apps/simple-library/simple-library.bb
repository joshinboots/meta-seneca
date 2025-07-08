SUMMARY = "Simple Library Application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=placeholder"

SRC_URI = "git://github.com/LNX500-summer-2025/Lab-Test.git;protocol=ssh;branch=LAB_TEST"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS += "gtest"

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/simple-library ${D}${bindir}
    install -m 0755 ${S}/simple-library-test ${D}${bindir}
}
