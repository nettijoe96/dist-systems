FROM ubuntu:latest

RUN apt-get -y update
RUN apt-get -y install openjdk-11-jdk

COPY build.sh /build.sh
COPY clean.sh /clean.sh
COPY pubsub/ /pubsub/

RUN /clean.sh
RUN /build.sh

RUN java pubsub/Client
