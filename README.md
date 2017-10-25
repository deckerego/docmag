# DocMag

DocMag bundles together Elasticsearch and FS Crawler together using Docker
to make searching across documents simple and efficient.


## Installation

Currently installation is performed entirely with `docker-compose`. To compose
the necessary containers:

    cd Docker
    docker-compose up -d

This should build & run the necessary containers, then begin indexing documents
from the tests/es directory. To shut down, execute:

    docker-compose down

To shut down and delete all the data generated from these containers:

    docker-compose down -v


## Searching & Querying Documents

FS Crawler is configured to use optical character recognition to parse documents,
and Kibana is provided for managment & querying capabilities.