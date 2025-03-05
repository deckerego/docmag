# Facile Search

**--> DocMag and DocIndex have been retired - take a look at [](https://docs.paperless-ngx.com/) as a replacement. <--**

Facile Search bundles together Elasticsearch and DocIndex
to make searching across documents simple and efficient.

## Requirements

Usually you won't want to build and run docmag locally, instead it is best to
run the docker container published at: https://hub.docker.com/r/deckerego/docmagui/

To run the containers needed for Facile Search you have three recommended options:
1. [Running on a single server with Docker Compose](docs/docker_compose.md)
1. [Installing within a cluster using Kubernetes and Helm](docs/kubernetes.md)
1. [Installing on a Synology NAS device](docs/synology.md)

See the [docs/](docs/) directory for instructions on installation and setup on these platforms.


## Using DocIndex to Index Files

The docidx daemon indexes files within Elasticsearch and prepares them for display - this is the
process that feeds docmag. More info is available at https://github.com/deckerego/docidx


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
