#!/bin/bash
set -e

if [ "$1" = 'java' ]; then
  java -jar /server.jar
fi

exec "$@"
