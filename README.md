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

Start C2C's Couchbase database loaded with historical data.
We assume that the server is running on `localhost` and the **Data Service** is accessible at the default `11210` port.

### Build:
`mvn clean install`
<p align="center">
<img src="./readme/build-successful.png" alt="Build Successful" height="300">
</p>

### Run:
`mvn exec:java -Dexec.mainClass="net.mekomsolutions.c2c.migration.Main"`
