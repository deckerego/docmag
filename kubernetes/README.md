# Kubernetes Specification Files

This directory includes Kubernetes specs for deploying a complete DocMag application,
including a single-node Elasticsearch instance. This is largely the same layout as
you would get with docker-compose, except distributed and managed with Kubernetes.


# Rebuilding a Search Index

After a large upgrade, or if you just want to rebuild your search index after some
weird failures, you can delete the search index and re-deploy the document indexer
using the Kubernetes workflow:

    kubectl proxy &
    curl -X DELETE http://localhost:8001/api/v1/namespaces/default/services/elasticsearch:http/proxy/docidx
    kubectl apply -f docidx-deployment.yaml

After docidx is re-deployed it will re-create the index and begin repopulating it
from scratch. *Be warned*: this will also _delete your list of tags and templates_, so images
will need to be re-tagged with newly created templates.
