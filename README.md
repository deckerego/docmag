# DocMag

DocMag bundles together Elasticsearch and FS Crawler together using Docker
to make searching across documents simple and efficient.


## Installation

Installation is performed with Maven and `docker-compose`. To compose
the necessary containers:

    mvn install
    docker-compose up -d

This should build & run the necessary containers, then begin indexing documents
from the tests/es directory. To shut down, execute:

    docker-compose down

To shut down and delete all the data generated from these containers:

    docker-compose down -v


## Searching & Querying Documents

To search within your documents, view thumbnails and open the full document 
navigate to `http://localhost:1080`. This should take you to the main search
interface, which will perform a full text search on your indexed documents.
