#!/bin/sh

mvn install
docker-compose -f docker-compose.yml -f docker-compose-devel.yml build docmagui
docker-compose -f docker-compose.yml -f docker-compose-devel.yml up -d --no-deps docmagui
