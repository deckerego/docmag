#!/bin/sh

export DOCUMENT_HOST_DIR=/Volumes/shared/Scanned
docker-compose up -d
caffeinate docker-compose logs -f
