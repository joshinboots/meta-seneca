SUMMARY = "This is a base image created as example for courses"
inherit core-image

IMAGE_LINGUAS = " "

LICENSE = "MIT"

IMAGE_INSTALL:append = " \
    usbutils \
    usbinit \
"

IMAGE_FEATURES:append = " \
    ssh-server-openssh \
    splash \
    tools-debug \
    tools-sdk \
"

DISTRO_FEATURES:append = " usbgadget"
MACHINE_FEATURES:append = " usbgadget usbhost"
