FROM ubuntu:latest

MAINTAINER john@deckerego.net

ADD https://repo1.maven.org/maven2/fr/pilato/elasticsearch/crawler/fscrawler/2.4/fscrawler-2.4.zip /tmp

RUN apt-get --assume-yes update
RUN apt-get --assume-yes install default-jre zip unzip tesseract-ocr netcat

RUN unzip -q /tmp/fscrawler-2.4.zip -d /opt
ADD launch_wait.sh /opt/fscrawler-2.4/bin/

VOLUME /usr/share/fscrawler/docs

VOLUME /etc/fscrawler
ADD _settings.json /etc/fscrawler/scanned/

WORKDIR /opt/fscrawler-2.4

ENTRYPOINT [ "./bin/fscrawler", "--config_dir", "/etc/fscrawler", "scanned", "--rest"]
