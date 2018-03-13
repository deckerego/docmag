# DocMag

DocMag bundles together Elasticsearch and DocIndex using Docker
to make searching across documents simple and efficient.


## Requirements

On your target OS, ensure docker-ce is installed along with Docker Compose:

https://docs.docker.com/engine/installation/

https://docs.docker.com/compose/install/

Usually you won't want to build and run docmag locally, instead it is best to
run the docker container published at: https://hub.docker.com/r/deckerego/docmagui/


## Installing with Docker Compose

You can install docmag and its dependencies (including Elasticsearch and docidx) using
just the docker-compose.yml file by extracting the latest release found at:

https://github.com/deckerego/docmag/releases

And executing:

    export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose up -d

Where the value of `DOCUMENT_HOST_DIR` is the directory you would like to scan and
index for searching within docmag. So `DOCUMENT_HOST_DIR=/tmp/fs` would recursively
search within the directory `/tmp/fs`. A sample startup script is offered as `start.sh`.

docker-compose should bring up docmag's user interface and Elasticsearch indexes, while also
indexing new documents in the background. By default the web application is running
locally on port 1080.


## Installing into Kubernetes with Helm

Installing into a Kubernetes cluster is pretty easy, thanks to Helm. Before you
install DocMag you will need to do two things however:

1. Set a username and password for the web interface, and
2. Create a persistent volume that points to your scanned documents

The first step can be accomplished using kubectl on the command line, as in:

    kubectl create secret generic docmagui-secret --from-literal=username='docmag' --from-literal=password='supersecretpassword'

The second step will require some Kubernetes administration. See the example at
docmag/document-volumes.yaml for a template to mount something over NFS. Once you
have settings you are happy with, you can create the persistent volume with:

    kubectl apply -f docmag/document-volumes.yaml

Once those two steps are done, add the DocMag repo and install with:

    helm repo add docmag http://docmag.deckerego.net
    helm install --name=docmag --set modsecurity.service.externalIP=10.200.1.10 docmag


## Upgrading

While it might be possible to do an in-place upgrade or update, I would recommend a full
re-start to deploy the latest version of each component. This would include:

1. From the directory you started docmag, issue `export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose down`
2. Extract the latest release docker-compose.yml from https://github.com/deckerego/docmag/releases
3. Re-start docmag as you did before: `export DOCUMENT_HOST_DIR=/mnt/documents && docker-compose up -d`


## Using docidx to Index Files

docidx indexes files within Elasticsearch and prepares them for display - this is the
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
