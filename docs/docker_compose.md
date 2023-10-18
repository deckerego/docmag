## Using with Docker Compose

If you are installing on a single server, Docker Compose is probably the easiest way
to get Facile Search running. On your target OS, ensure docker-ce is installed along with Docker Compose:

https://docs.docker.com/engine/installation/

https://docs.docker.com/compose/install/


### Installing with Compose

You can install docmag and its dependencies (including Elasticsearch and docidx) using
just the docker-compose.yml file by extracting the latest release found at:

https://github.com/deckerego/docmag/releases

And executing:

    export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose up -d

Where the value of `DOCUMENT_HOST_DIR` is the directory you would like to scan and
index for searching within docmag. So `DOCUMENT_HOST_DIR=/tmp/fs` would recursively
search within the directory `/tmp/fs`. A sample startup script is offered as `start.sh`.

docker-compose should bring up the docmag user interface and Elasticsearch indexes, while also
indexing new documents in the background. By default the web application is running
locally on port 80.


### Upgrading with Compose

While it might be possible to do an in-place upgrade or update, I would recommend a full
re-start to deploy the latest version of each component. This would include:

1. From the directory you started docmag, issue `export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose down`
2. Extract the latest release docker-compose.yml from https://github.com/deckerego/docmag/releases
3. Re-start docmag as you did before: `export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose up -d`
