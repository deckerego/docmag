#!/bin/sh

mvn test package
docker-compose build --build-arg JARFile=docmag-0.0.1-SNAPSHOT.jar .
docker-compose up --no-deps -d docmagui
