# DocMag

DocMag bundles together Elasticsearch and DocIndex using Docker
to make searching across documents simple and efficient.


## Requirements

On your target OS, ensure docker-ce is installed along with Docker Compose:

https://docs.docker.com/engine/installation/

https://docs.docker.com/compose/install/

Usually you won't want to build and run docidx locally, instead it is best to
run the docker container published at: https://hub.docker.com/r/deckerego/docmagui/


## Installing

You can install docmag and its dependencies (including Elasticsearch) using just the
docker-compose.yml file by cloning this repository and executing:

    export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose up -d

Where the value of `DOCUMENT_HOST_DIR` is the directory you would like to scan and
index for searching within docmag. So `DOCUMENT_HOST_DIR=/tmp/fs` would recursively
search within the directory `/tmp/fs`.

This should bring up docmag's user interface and Elasticsearch indexes, while also
indexing new documents in the background. By default the web application is running
locally on port 1080.

## Building and Testing Locally

Building the app and its necessary infrastructure locally is performed with
Maven and `docker-compose`.

To compose the necessary containers and configure Elasticsearch indexes:

    mvn install
    export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose -f docker-compose.yml -f docker-compose-devel.yml up -d
    cd es/
    ./configure.sh

The development instance of the composition config will expose Elasticsearch,
Spring Boot, and Kibana to local ports - so don't use this in a
production setting.

This should build & run the necessary containers, then begin indexing documents
from the directory specified as `DOCUMENT_HOST_DIR`. To shut down and delete the
Elasticsearch metadata generated from these containers, execute:

    export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose -f docker-compose.yml -f docker-compose-devel.yml down -v


## Searching and Querying Documents

To search within your documents, view thumbnails and open the full document
navigate to `http://localhost:1080`. This should take you to the main search
interface, which will perform a full text search on your indexed documents.
