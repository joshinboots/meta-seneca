SUMMARY = "Init script to launch pir-driver at boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://sensor_service.sh"
S = "${WORKDIR}"

inherit update-rc.d

INITSCRIPT_NAME = "pir-service"
INITSCRIPT_PARAMS = "defaults 98"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/sensor_service.sh ${D}${sysconfdir}/init.d/pir-service
}

