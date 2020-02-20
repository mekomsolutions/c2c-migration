package net.mekomsolutions.c2c.extract;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Route2Test extends CamelSpringTestSupport {

	private final String OUTPUT_RESOURCES_FOLDER = "src/test/resources/expectedOutput/";
	
	@Override
	protected AnnotationConfigApplicationContext createApplicationContext() {
		return new AnnotationConfigApplicationContext();
	}

	public void setUp() throws Exception {
		deleteDirectory("data/outbox");
		super.setUp();
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = super.createCamelContext();
		PropertiesComponent prop = context.getComponent(
				"properties", PropertiesComponent.class);
		prop.setLocation("application-test.properties");		
		return context;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			
			public void configure() throws Exception {
				from("couchbase:http://127.0.0.1:8091/?bucket=halix2&password=Admin123&username=Administrator")
				.to("seda:c2c");
			}
		};
	}

	@Ignore
	@Test
	public void shouldHandleContacts() throws Exception {

		String contacts = context.getTypeConverter().convertTo(
				String.class, new File("src/test/resources/h2-contacts-2.json"));

		template.sendBodyAndHeader("file://data/inbox", contacts,
				Exchange.FILE_NAME, "contacts.json");

		Thread.sleep(2000);


	}

}
