#!/bin/bash
# check the parameters with the dccd.properties file
java -jar dccd-reindex-0.1-SNAPSHOT.jar -store-name=dccd -store-url=http://localhost:8080/fedora -store-user=fedoraAdmin -store-password=###Fill-In-fedoraAdmin-Password### -search-engine-url=http://localhost:8080/solr -content-models=fedora-system:DCCD-PROJECT