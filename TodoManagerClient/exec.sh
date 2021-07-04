#!/bin/bash -x

cd `dirname $0`

BUILD_PATH="./out/production/TodoManagerClient"
LIB_PATH="./lib/gson-2.8.7.jar:./lib/lombok-1.18.20.jar:./lib/commons-lang3-3.12.0.jar"

export CLASSPATH="$BUILD_PATH:$LIB_PATH:$CLASSPATH"

if [ ! \( $# = 1 -a "$1" = "run" \) ]; then
  javac -d $BUILD_PATH  -sourcepath ./src ./src/com/example/*.java ./src/com/example/utils/*.java ./src/com/example/model/*.java
fi
if [ ! \( $# = 1 -a "$1" = "build" \) ]; then
  java com.example.Main 8080
fi
