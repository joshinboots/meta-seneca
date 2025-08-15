#!/bin/sh
### BEGIN INIT INFO
# Provides:          pir-service
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start PIR userspace driver
### END INIT INFO

DAEMON=/usr/bin/pir-driver
FIFO=/tmp/pir_fifo
ARGS="-b 5 -a 0x12"

case "$1" in
  start)
    [ -p "$FIFO" ] || mkfifo "$FIFO"
    start-stop-daemon --start --background --make-pidfile \
      --pidfile /var/run/pir-driver.pid --exec $DAEMON -- $ARGS
    ;;
  stop)
    start-stop-daemon --stop --pidfile /var/run/pir-driver.pid --retry 5
    ;;
  restart) $0 stop; $0 start ;;
  status)  [ -e /var/run/pir-driver.pid ] && echo "running" || echo "stopped" ;;
  *) echo "Usage: $0 {start|stop|restart|status}"; exit 1 ;;
esac
exit 0

