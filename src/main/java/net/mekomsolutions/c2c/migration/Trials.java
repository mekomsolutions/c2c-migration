package net.mekomsolutions.c2c.migration;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.json.JsonObject;

import net.mekomsolutions.c2c.migration.route.CouchbaseToOpenMRSRoute;
import reactor.core.publisher.Flux;

/**
 * Not used in production. Just a convenient class to run as an entry point
 * for data migration trials, in a development environment, on smaller sets
 *
 */
public class Trials {

	public static void main(String[] args) throws Exception {

		ActiveMQConnectionFactory connectionFactory =
				new ActiveMQConnectionFactoryConfigurer(AppProperties.getProperty("activemq.brokerUrl")).configure();

		// Initiate the JMS Queue with C2C Couchbase objects
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		MessageProducer producer = session.createProducer(session.createQueue("c2c.couchbase"));
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		Cluster cluster = Cluster.connect(AppProperties.getProperty("couchbase.host"),
				AppProperties.getProperty("couchbase.username"),
				AppProperties.getProperty("couchbase.password"));

		String query = "select * from halix2 where clinicKey = 'cli~H4' limit 100000 offset 0";

		ReactiveCluster reactiveCluster = cluster.reactive();
		reactiveCluster.query(query).flux()
		.flatMap(result -> {
			Flux<JsonObject> rows = result.rowsAs(JsonObject.class);
			return rows;
		}).subscribe(row -> {
			try {
				producer.send(session.createTextMessage(row.toString()));
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});

		CamelContext context = new DefaultCamelContext();

		context.addComponent("jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new CouchbaseToOpenMRSRoute());
		PropertiesComponent prop = context.getComponent(
				"properties", PropertiesComponent.class);
		prop.setLocation("classpath:application.properties");

		// start the route and let it do its work
		context.start();

	}
}
