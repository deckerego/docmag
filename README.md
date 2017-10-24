# DocMag - A Groovy Spring Boot + Elasticsearch Sample Webapp

This is a sample webapp constructed using:
- Spring Boot 2
- Groovy (for controllers, repositories, models, config)
- Elasticsearch 5.6
- Docker and Docker Compose
- Spring MVC, Data, Thymeleaf

I noticed pretty much all of the sample code for ES + Spring Boot was out of
date, using older versions of Spring Boot and the Elasticsearch API. On top of
that, very few examples used Groovy - and those that did only used it for
portions of the application. I wanted to try to have no Java code and use the
bleeding edge versions of everything, while using container composition to glue
the whole thing together.

I'm hoping to eventually turn this codebase into a document search engine -
but bear in mind that it will someday bitrot as well. This example forever tagged
just in case it comes in handy for others.

## Installation & Deployment

Building the sample app is performed with Maven, so install Java 8 + Maven
and execute:

    mvn package

This should build the app. Everything, including Elasticsearch, is deployed with
`docker-compose`. To compose the necessary containers, including deploying the
sample web application:

    docker-compose up -d

This should build & run the necessary containers, then begin indexing documents
from the tests/es directory. To shut down, execute:

    docker-compose down

To shut down and delete all the data generated from these containers:

    docker-compose down -v
