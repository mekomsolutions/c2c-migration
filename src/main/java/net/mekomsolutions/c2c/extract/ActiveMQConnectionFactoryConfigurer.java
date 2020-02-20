package net.mekomsolutions.c2c.extract;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.ActiveMQConnectionFactory;

import net.mekomsolutions.c2c.extract.entity.EntityWrapper;

public class ActiveMQConnectionFactoryConfigurer {

	private String brokerUrl;

	public ActiveMQConnectionFactoryConfigurer(String brokerUrl) {
		this.brokerUrl = brokerUrl;

	}

	public ActiveMQConnectionFactory configure() {

		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory(brokerUrl);

		connectionFactory.setUserName("admin");
		connectionFactory.setPassword("password");

		// Deal with security restrictions in latest ActiveMQ
		List<String> allowedPacakges = new ArrayList<>();
		allowedPacakges.add(EntityWrapper.class.getPackageName());
		allowedPacakges.add(List.class.getPackageName());
		allowedPacakges.add(Integer.class.getPackageName());

		connectionFactory.setTrustedPackages(allowedPacakges);

		return connectionFactory;

	}
}
