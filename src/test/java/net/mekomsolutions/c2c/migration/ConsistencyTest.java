package net.mekomsolutions.c2c.migration;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import net.mekomsolutions.c2c.migration.entity.Diagnosis;
import net.mekomsolutions.c2c.migration.entity.EntityWrapper;
import net.mekomsolutions.c2c.migration.entity.Visit;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEncounter;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;

public class ConsistencyTest extends CamelTestSupport {

	private static final String COUCHBASE_SELECTS = "/couchbase_selects";

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = super.createCamelContext();
		PropertiesComponent prop = context.getComponent(
				"properties", PropertiesComponent.class);
		prop.setLocation("application-test.properties");

		return context;
	}

	protected RouteBuilder createRouteBuilder() throws Exception { 
		return new RouteBuilder() {
			public void configure() throws Exception { 

				from("seda:queue:c2c-visit")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(Visit.class)
				.split(simple("${body.entities}"))
				.log("${body}")
				.to("mock:visit-messages");

				from("seda:queue:c2c-diagnosis")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(Diagnosis.class)
				.split(simple("${body.entities}"))
				.log("${body}")
				.to("mock:diagnosis-messages");
			}
		};
	}

	@Test
	public void shouldShareTheSameEncounter() throws Exception {

		// Load a visit
		template.sendBodyAndHeader("seda:queue:c2c-visit", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~visit/vst!~00~10000040cli~H3.json")
						.getFile())), Exchange.FILE_NAME, "vst!~00~10000040cli~H3.json");

		Thread.sleep(4000);

		UUID visitUuid = UUID.nameUUIDFromBytes("vst!~00~10000040cli~H3".getBytes());
		String visitLight = Utils.getModelClassLight("Visit", visitUuid);

		MockEndpoint mockVisits = getMockEndpoint("mock:visit-messages");
		mockVisits.expectedMessageCount(6);
		mockVisits.assertIsSatisfied(); 

		List<Exchange> visitMessages = mockVisits.getReceivedExchanges();

		EntityWrapper vBody1 = visitMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncEncounter encounter = (SyncEncounter) vBody1.getEntity();

		// Ensure the encounter has the correct reference to the visit
		assertTrue(encounter.getVisit().equals(visitLight));

		String encounterLight = Utils.getModelClassLight("Encounter",
				UUID.fromString(encounter.getUuid()));

		MockEndpoint mockDiagnoses = getMockEndpoint("mock:diagnosis-messages");

		// Load a Diagnosis for that visit
		template.sendBodyAndHeader("seda:queue:c2c-diagnosis", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~diagnosis/dia!~00~TwcAAAAAAAA~LMs.json")
						.getFile())), Exchange.FILE_NAME, "dia!~00~TwcAAAAAAAA~LMs.json");
		// Load the Chief Complaint for that visit
		template.sendBodyAndHeader("seda:queue:c2c-diagnosis", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~diagnosis/dia!~00~TwcAAAAAAAA~K8s.json")
						.getFile())), Exchange.FILE_NAME, "dia!~00~TwcAAAAAAAA~K8s.json");

		mockDiagnoses.expectedMessageCount(4);
		mockDiagnoses.assertIsSatisfied(); 

		EntityWrapper dBody0 = mockDiagnoses.getReceivedExchanges().get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation visitDiagnosis = (SyncObservation) dBody0.getEntity();
		// Ensure the diagnosis has the correct encounter
		assertTrue(visitDiagnosis.getEncounter().equals(encounterLight));

		mockDiagnoses.expectedMessageCount(5);
		mockDiagnoses.assertIsSatisfied(); 

		EntityWrapper dBody4 = mockDiagnoses.getReceivedExchanges().get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaint = (SyncObservation) dBody4.getEntity();
		// Ensure the chief complain has the correct encounter
		assertTrue(chiefComplaint.getEncounter().equals(encounterLight));

	}
}
