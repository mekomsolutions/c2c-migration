package net.mekomsolutions.c2c.migration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import net.mekomsolutions.c2c.migration.producer.C2CExtractProducer;
import net.mekomsolutions.c2c.migration.route.CouchbaseToOpenMRS;

public class Main {

	public static void main(String[] args) throws Exception {

		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactoryConfigurer("tcp://localhost:62616").configure();

		// Initiate the JMS Queue with C2C Couchbase objects
		C2CExtractProducer extractor = new  C2CExtractProducer(connectionFactory);
		extractor.start();

		CamelContext context = new DefaultCamelContext();

		context.addComponent("jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new CouchbaseToOpenMRS());
		PropertiesComponent prop = context.getComponent(
				"properties", PropertiesComponent.class);
		prop.setLocation("classpath:application.properties");

		// start the route and let it do its work
		context.start();

	}
}