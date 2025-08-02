IMAGE_BOOT_FILES = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin k3-am625-beagleplay.dtb"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DISTRO_FEATURES:append = " usbgadget wayland opengl pam x11"

CORE_IMAGE_BASE_INSTALL += "gtk+3-demo"

CORE_IMAGE_BASE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'weston-xwayland matchbox-terminal', '', d)}"

IMAGE_INSTALL:append = " \
    weston \
    matchbox-keyboard \
    cairo  \
    libgbm \
    libdrm \
    wayland \
    mesa \
    sensor-dash \
"

