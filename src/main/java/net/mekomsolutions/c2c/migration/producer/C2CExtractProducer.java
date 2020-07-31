package net.mekomsolutions.c2c.migration.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.json.JsonObject;

import net.mekomsolutions.c2c.migration.AppProperties;
import reactor.core.publisher.Flux;

public class C2CExtractProducer {

	private ActiveMQConnectionFactory connectionFactory;

	public C2CExtractProducer(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void start() {

		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(session.createQueue("c2c.couchbase"));
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			Cluster cluster = Cluster.connect(AppProperties.getProperty("couchbase.host"),
					AppProperties.getProperty("couchbase.username"), AppProperties.getProperty("couchbase.password"));

			List<String> supportedEntities = new ArrayList<String>();
			supportedEntities.add("dlm~00~c2c~contact");
			supportedEntities.add("dlm~00~c2c~patient");
			supportedEntities.add("dlm~00~c2c~visit");
			supportedEntities.add("dlm~00~c2c~diagnosis");
			supportedEntities.add("dlm~00~c2c~medicineevent");
			supportedEntities.add("dlm~00~c2c~labtest");

			List<String> uniqueImportSources = supportedEntities.stream()
					// Generate queries to get each entity import source
					.map(entity -> "select distinct raw importSource from "
							+ AppProperties.getProperty("couchbase.bucket.name") + " where dataElementKey = '" + entity
							+ "'")
					.collect(Collectors.toList()).stream()
					// Run the queries
					.map(query -> cluster.query(query).rowsAs(String.class))
					// Merge that into one unique import source.
					.flatMap(x -> x.stream()).collect(Collectors.toList());

			// Use the Async cluster
			ReactiveCluster reactiveCluster = cluster.reactive();

			uniqueImportSources.stream()
			// Generate queries to get data from each import source and each entity
			.flatMap(importSource -> supportedEntities.stream()
					.map(entity -> "select * from " + AppProperties.getProperty("couchbase.bucket.name")
					+ " where importSource = '" + importSource + "'" + " and dataElementKey = '"
					+ entity + "'"))
			// Run the queries asynchronously
			.forEach(query -> {
				reactiveCluster.query(query).flux().flatMap(result -> {
					Flux<JsonObject> rows = result.rowsAs(JsonObject.class);
					return rows;
				}).subscribe(row -> {
					try {
						producer.send(session.createTextMessage(row.toString()));
					} catch (JMSException e) {
						e.printStackTrace();
					}
				});
			});

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}
}
