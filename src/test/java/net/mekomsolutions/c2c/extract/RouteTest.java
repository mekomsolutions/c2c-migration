package net.mekomsolutions.c2c.extract;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.mekomsolutions.c2c.extract.route.Route1;

public class RouteTest extends CamelSpringTestSupport {

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
		return new Route1();
	}

	@Test
	public void shouldHandleContacts() throws Exception {

		String contacts = context.getTypeConverter().convertTo(
				String.class, new File("src/test/resources/h2-contacts-2.json"));

		template.sendBodyAndHeader("file://data/inbox", contacts,
				Exchange.FILE_NAME, "contacts.json");

		Thread.sleep(2000);

		String expectedOutputFolder = OUTPUT_RESOURCES_FOLDER + "Contacts";
		
		JSONObject expectedMessage1 = context.getTypeConverter().convertTo(
				JSONObject.class, new File(expectedOutputFolder + "/expectedPersonAttribute1.json"));
		File message1 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-140b4bbd-259e-3bac-86b9-9bc74dbff674");
		assertEquals(expectedMessage1, context.getTypeConverter().convertTo(
				JSONObject.class, message1));

		JSONObject expectedMessage2 = context.getTypeConverter().convertTo(
				JSONObject.class, new File(expectedOutputFolder + "/expectedPersonAttribute2.json"));
		File message2 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-faadc59c-b240-3caa-882a-db61c614b314");
		assertEquals(expectedMessage2, context.getTypeConverter().convertTo(
				JSONObject.class, message2));

		JSONObject expectedMessage3 = context.getTypeConverter().convertTo(
				JSONObject.class, new File(expectedOutputFolder + "/expectedPersonAttribute3.json"));
		File message3 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-5944ab2e-9cc1-307e-897d-307949892e71");
		assertEquals(expectedMessage3, context.getTypeConverter().convertTo(
				JSONObject.class, message3));

	}

	@Test
	public void shouldHandlePatients() throws Exception {

		String patients = context.getTypeConverter().convertTo(
				String.class, new File("src/test/resources/h2-patients-10.json"));

		template.sendBodyAndHeader("file://data/inbox", patients,
				Exchange.FILE_NAME, "patients.json");

		Thread.sleep(2000);

		String expectedOutputFolder = OUTPUT_RESOURCES_FOLDER + "Patients";
		
		JSONObject expectedMessage1 = context.getTypeConverter().convertTo(
				JSONObject.class, new File(expectedOutputFolder + "/expectedPatient1.json"));
		File message1 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PatientModel-05eeac8d-100e-3fd7-a258-a33a663661c1");
		assertEquals(expectedMessage1, context.getTypeConverter().convertTo(
				JSONObject.class, message1));

		JSONObject expectedMessage2 = context.getTypeConverter().convertTo(
				JSONObject.class, new File(expectedOutputFolder + "/expectedPersonName1.json"));
		File message2 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonNameModel-9f8ee9e2-f855-3040-95a0-a16b82fbf36b");
		assertEquals(expectedMessage2, context.getTypeConverter().convertTo(
				JSONObject.class, message2));

		JSONObject expectedMessage3 = context.getTypeConverter().convertTo(
				JSONObject.class, new File(expectedOutputFolder + "/expectedPersonAttribute1.json"));
		File message3 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonAttributeModel-0385d29d-29bc-3370-9dda-adee2d442491");
		assertEquals(expectedMessage3, context.getTypeConverter().convertTo(
				JSONObject.class, message3));

	}

}
