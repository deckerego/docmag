# DocMag

DocMag bundles together Elasticsearch and FS Crawler together using Docker
to make searching across documents simple and efficient.


## Building & Testing Locally

Building the app and its necessary infrastructure locally is performed with
Maven and `docker-compose`. To compose the necessary containers and configure
Elasticsearch indexes:

    mvn install
    docker-compose -f docker-compose.yml -f docker-compose-devel.yml up -d
    cd es/
    ./configure.sh

The development instance of the composition config will expose Elasticsearch,
Spring Boot, fscrawler and Kibana to local ports - so don't use this in a
production setting.

This should build & run the necessary containers, then begin indexing documents
from the tests/es directory. To shut down, execute:

    docker-compose -f docker-compose.yml -f docker-compose-devel.yml down

To shut down and delete all the data generated from these containers:

    docker-compose -f docker-compose.yml -f docker-compose-devel.yml down -v


## Searching & Querying Documents

To search within your documents, view thumbnails and open the full document
navigate to `http://localhost:1080`. This should take you to the main search
interface, which will perform a full text search on your indexed documents.
