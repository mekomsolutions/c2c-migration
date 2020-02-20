package net.mekomsolutions.c2c.extract.producer;

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
			Destination destination = session.createQueue("c2c.couchbase.halix2");

			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create messages
			Cluster cluster = Cluster.connect("localhost", "Administrator", "Admin123");
			QueryResult result = cluster.query("select * from halix2 where objType = 'SolutionObject' limit 10");
			List<JsonObject> results = result.rowsAsObject();
			
			for (JsonObject entry : results) {
				TextMessage message = session.createTextMessage(entry.toString());
				producer.send(message);
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
