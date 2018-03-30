FROM ubuntu:latest

LABEL maintainer="john@deckerego.net"

RUN apt-get --assume-yes update
RUN apt-get --assume-yes install default-jre

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/docmag/docmagui.jar
RUN mkdir -p /opt/docmag/config

WORKDIR /opt/docmag

ENTRYPOINT [ "java", "-jar", "docmagui.jar"]
