# Kubernetes Specification Files

This directory includes Kubernetes specs for deploying a complete DocMag application,
including a single-node Elasticsearch instance. This is largely the same layout as
you would get with docker-compose, except distributed and managed with Kubernetes.


## Initial Provisioning

You can install docmag and its dependencies (including Elasticsearch and docidx)
by extracting the latest release found at:

https://github.com/deckerego/docmag/releases

While within the extracted directory, the entire stack for DocMag can be
provisioned on a new cluster by issuing:

    kubectl create secret generic docmagui-secret --from-literal=username='docmag' --from-literal=password='supersecretpassword'
    kubectl create -f kubernetes

This should provision & deploy everything specified within the kubernetes/ directory.
Note that you should provide your own username and password - this is what will
be used to authenticate users. Currently DocMag only supports a single login.


## Upgrades

Upgrades can be applied by fetching the latest release, extracting the files,
then issuing:

    kubectl apply -f kubernetes


## Rebuilding a Search Index

After a large upgrade, or if you just want to rebuild your search index after some
weird failures, you can delete the search index and re-deploy the document indexer.
*THIS WILL DELETE YOUR EXISTING TAGS AND TEMPLATES*, so use these steps with caution:

    kubectl proxy &
    curl -X DELETE http://localhost:8001/api/v1/namespaces/default/services/elasticsearch:http/proxy/docidx
    kubectl replace --force -f kubernetes/docidx-deployment.yaml

After docidx is re-deployed it will re-create the index and begin repopulating
search entries from scratch. This may take several hours based on the number of
documents you have available.
