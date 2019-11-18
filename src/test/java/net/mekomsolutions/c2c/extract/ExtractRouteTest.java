package net.mekomsolutions.c2c.extract;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.mekomsolutions.c2c.extract.Entity.EntityWrapper;
import net.mekomsolutions.c2c.extract.Entity.Person;

public class ExtractRouteTest extends CamelSpringTestSupport {

	@Override
	protected AnnotationConfigApplicationContext createApplicationContext() {
		return new AnnotationConfigApplicationContext();
	}

	@Test
	public void testRoute() throws Exception {

		MockEndpoint mockSplit = getMockEndpoint("mock:split");
		mockSplit.setAssertPeriod(500);
		mockSplit.expectedMessageCount(10);

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
				.to("seda:contact");

				from("seda:contact").convertBodyTo(Person.class)
				.setHeader("modelClassName", simple("${body.getModelClassName}"))
				.setHeader("uuid", simple("${body.getUuid}"))
				.to("seda:save");

				from("seda:save").convertBodyTo(EntityWrapper.class).marshal().json(JsonLibrary.Jackson)
				.to("file:data/outbox/?fileName=${header.modelClassName}-${header.uuid}")
				.log("Converted: ${body}")
				.to("mock:split");

			}
		};
	}


}
