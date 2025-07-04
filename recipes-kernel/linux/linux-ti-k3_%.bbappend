FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://usb-gadget.cfg"
KERNEL_FEATURES:append = " usb-gadget.cfg"
