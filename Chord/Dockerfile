FROM ubuntu:latest

RUN apt-get -y update
RUN apt-get -y install openjdk-11-jdk net-tools iputils-ping

COPY src/ /src/

RUN javac src/*.java

