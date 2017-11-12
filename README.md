# DocMag

DocMag bundles together Elasticsearch and FS Crawler together using Docker
to make searching across documents simple and efficient.


## Requirements

On your target OS, ensure docker-ce is installed along with Docker Compose:

https://docs.docker.com/engine/installation/
https://docs.docker.com/compose/install/


## Building & Testing Locally

Building the app and its necessary infrastructure locally is performed with
Maven and `docker-compose`.

Environment-specific details (like the directory of your documents) is stored
within the `.env` file. Make sure to edit this file after cloning the repository.

To compose the necessary containers and configure Elasticsearch indexes:

    mvn install
    docker-compose -f docker-compose.yml -f docker-compose-devel.yml up -d
    cd es/
    ./configure.sh

The development instance of the composition config will expose Elasticsearch,
Spring Boot, fscrawler and Kibana to local ports - so don't use this in a
production setting.

This should build & run the necessary containers, then begin indexing documents
from the directory specified in `.env`. To shut down and delete the Elasticsearch
metadata generated from these containers, execute:

    docker-compose -f docker-compose.yml -f docker-compose-devel.yml down -v


## Searching & Querying Documents

To search within your documents, view thumbnails and open the full document
navigate to `http://localhost:1080`. This should take you to the main search
interface, which will perform a full text search on your indexed documents.
