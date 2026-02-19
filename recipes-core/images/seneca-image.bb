SUMMARY = "This is a base image created as example for courses"
inherit core-image

LICENSE = "MIT"
IMAGE_LINGUAS = " "

IMAGE_BOOT_FILES = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin k3-am625-beagleplay.dtb"

# Packages to include
IMAGE_INSTALL:append = " \
    usbutils \
    usbinit \
    i2c-tools \
    pir-driver \
    pir-service \
    sensor-dashboard \
    weston \
    weston-init \
    matchbox-keyboard \
    cairo \
    libgbm \
    libdrm \
    wayland \
    mesa \
"

IMAGE_FEATURES:append = " \
    ssh-server-openssh \
    splash \
    tools-debug \
    tools-sdk \
"

DISTRO_FEATURES:append = " usbgadget wayland opengl pam x11"
MACHINE_FEATURES:append = " usbgadget usbhost"

CORE_IMAGE_BASE_INSTALL += "gtk+3-demo"
CORE_IMAGE_BASE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'weston-xwayland matchbox-terminal', '', d)}"

