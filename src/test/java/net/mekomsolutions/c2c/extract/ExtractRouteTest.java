package net.mekomsolutions.c2c.extract;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.mekomsolutions.c2c.extract.Entity.Contact;
import net.mekomsolutions.c2c.extract.Entity.Patient;

public class ExtractRouteTest extends CamelSpringTestSupport {

	@Override
	protected AnnotationConfigApplicationContext createApplicationContext() {
		return new AnnotationConfigApplicationContext();
	}

	@Test
	public void testRoute() throws Exception {

		MockEndpoint mockSplit = getMockEndpoint("mock:split");
		mockSplit.setAssertPeriod(500);
		mockSplit.expectedMessageCount(30);
		assertMockEndpointsSatisfied();

	}

	protected RouteBuilder createRouteBuilder() throws Exception {

		PropertiesComponent prop = context.getComponent(
				"properties", PropertiesComponent.class);
		prop.setLocation("classpath:application.properties");		

		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file:data/inbox/?noop=true")
				.split().jsonpath("$.[*].{{couchdb.bucket.name}}").streaming()
				.setHeader("type",simple("${body[dataElementKey]}"))
				.choice()
				.when(header("type").isEqualTo("dlm~00~c2c~contact"))
				.to("seda:contact")
				.when(header("type").isEqualTo("dlm~00~c2c~patient"))
				.to("seda:patient");

				from("seda:contact").convertBodyTo(Contact.class)
				.split(simple("${body.entities}"))
				.to("seda:save");

				from("seda:patient").convertBodyTo(Patient.class)
				.split(simple("${body.entities}"))
				.to("seda:save");
				
				from("seda:save")
				.setHeader("modelClass", simple("${body.modelClass}"))
				.setHeader("uuid", simple("${body.uuid}"))
				.marshal().json(JsonLibrary.Jackson)
				.to("file:data/outbox/?fileName=${header.modelClass}-${header.uuid}")
				.log("As a JSON: ${body}")
				.to("mock:split");

			}
		};
	}


}
