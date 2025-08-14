DESCRIPTION = "Seneca Labs image for BeaglePlay"
LICENSE = "MIT"
inherit core-image

# Handy features from the lab notes
IMAGE_FEATURES:append = " ssh-server-openssh splash"

# Pin exactly what we want (no packagegroup-core-full-cmdline)
IMAGE_INSTALL:append = " \
    weston \
    weston-seneca-config \
    matchbox-keyboard \
    cairo \
    libgpiod \
    libgpiod-tools \
    simple-library \
    sensor-dash \
    reverse-parking \
"
