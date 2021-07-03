#!/bin/bash -x

cd `dirname $0`

BUILD_PATH="./out/production/TodoManagerClient"
LIB_PATH="./lib/gson-2.8.7.jar:./lib/lombok-1.18.20.jar"

export CLASSPATH="$BUILD_PATH:$LIB_PATH:$CLASSPATH"

javac -d $BUILD_PATH  -sourcepath ./src ./src/com/example/*.java ./src/com/example/utils/*.java
java com.example.Main 8080
