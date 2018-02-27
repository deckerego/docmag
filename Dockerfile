FROM ubuntu:latest

MAINTAINER john@deckerego.net

RUN apt-get --assume-yes update
RUN apt-get --assume-yes install default-jre

ARG DOCMAGUI_VERSION=0.4.3
ADD target/docmag-${DOCMAGUI_VERSION}.jar /opt/docmag/docmagui.jar

WORKDIR /opt/docmag

ENTRYPOINT [ "java", "-jar", "docmagui.jar"]
