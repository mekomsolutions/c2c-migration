package net.mekomsolutions.c2c.migration.producer;

import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;

import net.mekomsolutions.c2c.migration.AppProperties;

public class C2CExtractProducer {

	private ActiveMQConnectionFactory connectionFactory;

	public C2CExtractProducer(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void start() {

		try {
			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue("c2c.couchbase");

			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create messages
			Cluster cluster = Cluster.connect(AppProperties.getProperty("couchbase.host"), 
					AppProperties.getProperty("couchbase.username"), 
					AppProperties.getProperty("couchbase.password"));


			{
				QueryResult result = cluster.query("select * from "
						+ AppProperties.getProperty("couchbase.bucket.name")
						+ " where objType = 'SolutionObject' "
						+ "and dataElementKey = 'dlm~00~c2c~contact' limit "
						+ AppProperties.getProperty("couchbase.query.limit"));
				List<JsonObject> results = result.rowsAsObject();

				for (JsonObject entry : results) {
					TextMessage message = session.createTextMessage(entry.toString());
					producer.send(message);
				}
			}
			{
				QueryResult result = cluster.query("select * from "
						+ AppProperties.getProperty("couchbase.bucket.name")
						+ " where objType = 'SolutionObject' "
						+ "and dataElementKey = 'dlm~00~c2c~patient' limit "
						+ AppProperties.getProperty("couchbase.query.limit"));
				List<JsonObject> results = result.rowsAsObject();

				for (JsonObject entry : results) {
					TextMessage message = session.createTextMessage(entry.toString());
					producer.send(message);
				}
			}
			{
				QueryResult result = cluster.query("select * from "
						+ AppProperties.getProperty("couchbase.bucket.name")
						+ " where objType = 'SolutionObject' "
						+ "and dataElementKey = 'dlm~00~c2c~diagnosis' limit "
						+ AppProperties.getProperty("couchbase.query.limit"));
				List<JsonObject> results = result.rowsAsObject();

				for (JsonObject entry : results) {
					TextMessage message = session.createTextMessage(entry.toString());
					producer.send(message);
				}
			}
			{
				QueryResult result = cluster.query("select * from "
						+ AppProperties.getProperty("couchbase.bucket.name")
						+ " where objType = 'SolutionObject' "
						+ "and dataElementKey = 'dlm~00~c2c~visit' limit "
						+ AppProperties.getProperty("couchbase.query.limit"));
				List<JsonObject> results = result.rowsAsObject();

				for (JsonObject entry : results) {
					TextMessage message = session.createTextMessage(entry.toString());
					producer.send(message);
				}
			}

			// Clean up
			session.close();
			connection.close();
		}
		catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}
}
