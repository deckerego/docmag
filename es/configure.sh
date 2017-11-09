#!/bin/bash

ES_HOST='localhost'
ES_PORT='9200'

ES_SERVER="$ES_HOST:$ES_PORT"

echo -n "Global index settings: "
curl -X PUT "http://$ES_SERVER/_settings" -d @index.json
echo

echo -n "Universal template settings: "
curl -X PUT "http://$ES_SERVER/_template/univeral_template" -d @template.json
echo

echo -n "Kibana monitoring template settings: "
curl -X PUT "http://$ES_SERVER/_template/custom_monitoring" -d @kibana.json
echo