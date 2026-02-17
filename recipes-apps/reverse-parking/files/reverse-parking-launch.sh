#!/bin/sh
CONF="/etc/reverse-parking.conf"

# defaults if the conf file isn't edited yet
CHIP=0
TRIG=23
ECHO=24
BUZZ=25

# shell-source the conf if present (CHIP/TRIG/ECHO/BUZZ vars)
[ -f "$CONF" ] && . "$CONF"

exec /usr/bin/reverse-parking "$CHIP" "$TRIG" "$ECHO" "$BUZZ"
