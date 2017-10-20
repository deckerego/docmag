#!/bin/bash

USERNAME='elastic'
PASSWORD='changeme'
ES_HOST='localhost'
ES_PORT='9200'

BASIC_LOGIN="$USERNAME:$PASSWORD"
ES_SERVER="$ES_HOST:$ES_PORT"

echo -n "Global index settings: "
curl -u "$BASIC_LOGIN" -X PUT "http://$ES_SERVER/_settings" -d @index.json
echo

echo -n "Universal template settings: "
curl -u "$BASIC_LOGIN" -X PUT "http://$ES_SERVER/_template/univeral_template" -d @template.json
echo
