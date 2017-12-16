FROM docker.elastic.co/elasticsearch/elasticsearch:5.6.4

MAINTAINER john@deckerego.net

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-phonetic
COPY --chown=elasticsearch:elasticsearch elasticsearch.yml /usr/share/elasticsearch/config/
