#!/bin/sh

export DOCUMENT_HOST_DIR=$PWD/tests/docidx
docker-compose up -d
caffeinate docker-compose logs -f
