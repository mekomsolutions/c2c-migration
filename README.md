# C2C Migration

Java project to migrate Care 2 Communities' historical data to OpenMRS. This project relies heavily on the [Apache Camel](https://camel.apache.org/) library, [Apache ActiveMQ](https://activemq.apache.org/) and the [OpenMRS DB Sync](https://github.com/openmrs/openmrs-dbsync) project.

<p align="left">
<img src="./readme/camel-long.png" alt="Apcahe Camel" height="80">
&nbsp;&nbsp;&nbsp
<img src="./readme/active-mq-long.png" alt="Apache ActiveMQ" height="80">
&nbsp;&nbsp;
<img src="./readme/openmrs-long.png" alt="OpenMRS Logo" height="80">
</p>

## Run the project:

### Build:
`mvn clean install`
<p align="center">
<img src="./readme/build-successful.png" alt="Build Successful" height="300">
</p>

### Run:

#### Start Couchbase
Start C2C's Couchbase database loaded with historical data.
We assume that the server is running on `localhost` and the **Data Service** is accessible at the default `11210` port.

#### Start ActiveMQ
C2C Migration uses a standalone instance of ActiveMQ to store the messages between routes. Run ActiveMQ locally using the Docker Compose project in [docker/activemq/](docker/activemq/)
```
cd docker/activemq
docker-compose up
```

#### Run C2C Migration
Then run the program:

`mvn exec:java -Dexec.mainClass="net.mekomsolutions.c2c.migration.Main"`
