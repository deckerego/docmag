#!/bin/sh

mvn test package
docker build --build-arg JARFile=docmag-0.0.1-SNAPSHOT.jar -t docmag/docmagui .
docker-compose up -d
