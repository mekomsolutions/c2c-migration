package net.mekomsolutions.c2c.migration.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import net.mekomsolutions.c2c.migration.entity.Contact;
import net.mekomsolutions.c2c.migration.entity.Patient;

public class Route1 extends RouteBuilder {
	
	public void configure() throws Exception {
		
		from("jms:queue:c2c.couchbase.halix2")
		.split().jsonpath("$.{{couchdb.bucket.name}}").streaming()
		.setHeader("type",simple("${body[dataElementKey]}"))
		.choice()
		.when(header("type").isEqualTo("dlm~00~c2c~contact"))
		.to("jms:queue:c2c-contact")
		.when(header("type").isEqualTo("dlm~00~c2c~patient"))
		.to("jms:queue:c2c-patient");

		from("jms:queue:c2c-contact").convertBodyTo(Contact.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("jms:queue:c2c-patient").convertBodyTo(Patient.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("jms:queue:openmrs-save")
		.setHeader("modelClass", simple("${body.modelClass}"))
		.setHeader("uuid", simple("${body.uuid}"))
		.marshal().json(JsonLibrary.Jackson)
		.to("file:data/outbox/?fileName=${header.modelClass}-${header.uuid}");
	}
}
