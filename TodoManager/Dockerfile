FROM gradle:7-jdk8

RUN apt-get update

RUN mkdir /app

WORKDIR /app

COPY . /app

RUN gradle clean build
