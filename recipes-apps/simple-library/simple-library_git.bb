SUMMARY = "Simple C++ library for Lab 9"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=778c13aea1f6f0167c18e40f8ef05bc7"

SRC_URI = "git://github.com/rashikrahmanishmum/simple-library.git;branch=main;protocol=https"
SRCREV = "3b24975c5f57bcc40e68aa427efb9b03bcc8476d"

PV = "1.0+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig


DEPENDS += "gtest nlohmann-json"

EXTRA_OECMAKE = "-DBUILD_TESTING=ON"

