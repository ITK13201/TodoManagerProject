#! /usr/bin/env sh

javac -d out/production/client -sourcepath src src/Main.java
java -classpath out/production/client Main 8080
