FROM ubuntu:latest

MAINTAINER john@deckerego.net

RUN apt-get --assume-yes update
RUN apt-get --assume-yes install default-jre

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/docmag/docmagui.jar

WORKDIR /opt/docmag

ENTRYPOINT [ "java", "-jar", "docmagui.jar"]
