## Hosting Facile Search using Kubernetes

Facile Search can be easily used with Kubernetes or with Docker Compose, and has been
tested on bare-metal (non-cloud) installations of Kubernetes clusters.


### Installing with Helm

Installing into a Kubernetes cluster is pretty easy, thanks to Helm. Before you
install Facile Search you will need to do two things however:

1. Set a username and password for the web interface, and
2. Create a persistent volume that points to your scanned documents

The first step can be accomplished using kubectl on the command line, as in:

    kubectl create secret generic docmagui-secret --from-literal=username='docmag' --from-literal=password='supersecretpassword'

The second step will require some Kubernetes administration. See the example at
docmag/document-volumes.yaml for a template to mount something over NFS. Once you
have settings you are happy with, you can create the persistent volume with:

    kubectl apply -f docmag/document-volumes.yaml

Once those two steps are done, add the DocMag repo and install with:

    helm repo add docmag http://helm.deckerego.net
    helm install docmag/docmag --name=docmag --set ingress.hosts={node.yourdomain.egg}

Note `ingress.hosts` needs to be specified in order to expose the application
outside of your cluster.


### Securing Facile Search with Helm

Note that while Docker Compose places the web interface behind a web application
firewall, the default Helm installation _DOES NOT_. It is the job of the Ingress
controller to provide a web application firewall and SSL termination. See
docmag/templates/NOTES.txt for additional details.

To serve the web interface over TLS, first create a Secret within Kubernetes
that contains the TLS certificate and key. An example of how to do this with
the nginx Ingress controller might be:

    kubectl create secret tls docmag-tls \
    --key etc/live/yourdomain.egg/privkey.pem \
    --cert etc/live/yourdomain.egg/fullchain.pem

Once the secret is installed, an install command like the following can be used:

    helm install docmag/docmag --name=docmag \
    --set ingress.hosts={node.yourdomain.egg} \
    --set ingress.tls.secretName=docmag-tls
