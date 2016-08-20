#!/bin/sh

GRADLE_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=5006,server=y,suspend=y" gradle jettyRunWar

