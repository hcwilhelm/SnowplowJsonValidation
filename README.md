# Snowplow Json Validation Service

## How to build it 

Make sure you have at least Java 11 SDK installed and configured for you environment. 

``sbt compile``

## Run the Service

``sbt run``

## Test if service is running 

``http localhost:8080/health``

Response shoule be ``Server is up and running``
