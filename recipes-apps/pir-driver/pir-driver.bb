SUMMARY = "Userspace PIR driver -> writes JSON motion events to /tmp/pir_fifo"
HOMEPAGE = "https://github.com/joshinboots/pir-driver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "1.0+git${SRCPV}"

# Pull your code from GitHub (main branch). During dev we use AUTOREV.
SRC_URI = "git://github.com/joshinboots/pir-driver.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake

# CMakeLists installs the binary to /usr/bin (RUNTIME DESTINATION bin)
FILES:${PN} += "${bindir}/pir-driver"
