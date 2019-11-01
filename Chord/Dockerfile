FROM ubuntu:latest

RUN apt-get -y update
RUN apt-get -y install openjdk-11-jdk

COPY src/ /src/

RUN javac src/*.java

