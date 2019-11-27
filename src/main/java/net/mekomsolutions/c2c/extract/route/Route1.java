package net.mekomsolutions.c2c.extract.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import net.mekomsolutions.c2c.extract.entity.Contact;
import net.mekomsolutions.c2c.extract.entity.Patient;

public class Route1 extends RouteBuilder {
	
	public void configure() throws Exception {
		from("file:data/inbox/")
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
		.to("file:data/outbox/?fileName=${header.modelClass}-${header.uuid}");
	}
}
