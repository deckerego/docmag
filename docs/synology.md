## Running Facile Search With Synology's Docker Support

1. Download images
2. Create new shared network
3. Create folder inside of shared folder
4. For current version of ES, chown -R 1000:100 esdata01/
5. Set ES environment variables
6. Start ES
7. Create read-only volume endpoint for docidx & docmagui
8. Add docidx & docmagui environment variables
9. Update CRAWLER_WAITSECONDS
10. Portmap 8080 to 8080
11. Add modsecurity environment variables
12. Portmap 80 to 1080
13. Customize firewall rules to allow 1080 from LAN
14. Customize firewall rules to allow 8080,9300 from esnet01
15. Add TLS certificates in a locked-down directory
16. Mount the TLS cert directory as a read-only mount point
17. Set the SSL environment variables
18. Portmap 443 to 1443
19. Customize firewall rules to allow 1443 from LAN

modproxy env vars to change from https://hub.docker.com/r/owasp/modsecurity :
BACKEND: http://deckerego-docmagui1:8080
PORT: 80
PROXY_PRESERVE_HOST: on
PROXY_SSL_CERT_KEY: private key
PROXY_SSL_CERT: server cert
PROXY_SSL: on
PROXY_PRESERVE_HOST: off
REMOTEIP_INT_PROXY: 192.168.129.0/24
REQ_HEADER_FORWARDED_PROTO: https
SERVER_NAME: nasego
SSL_ENGINE: on
SSL_PORT: 443
PARANOIA: 3
ANOMALY_INBOUND: 5
ANOMALY_OUTBOUND: 4
SERVERNAME: deckerego-modsecurity-crs1
