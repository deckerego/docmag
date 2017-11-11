#!/bin/bash

ES_HOST='localhost'
ES_PORT='9200'

ES_SERVER="$ES_HOST:$ES_PORT"

echo -n "Global index settings: "
curl -X PUT "http://$ES_SERVER/_settings" -d '{"index":{"number_of_replicas":"0"}}'
echo

echo -n "Universal template settings: "
curl -X PUT "http://$ES_SERVER/_template/univeral_template" -d '{"template":".monitoring-*","order":1,"settings":{"number_of_shards":1,"number_of_replicas":0}}'
echo

echo -n "Kibana monitoring template settings: "
curl -X PUT "http://$ES_SERVER/_template/custom_monitoring" -d '{"template":"*","order":1,"settings":{"index":{"number_of_shards":2,"number_of_replicas":0}}}'
echo
