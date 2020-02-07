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

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mekomsolutions.c2c.extract.route.Route1;

public class Route1Test extends CamelSpringTestSupport {

	private final String OUTPUT_RESOURCES_FOLDER = "/expectedOutput";
	
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

		ObjectMapper mapper = new ObjectMapper();
		
		String expectedOutputFolder = OUTPUT_RESOURCES_FOLDER + "/Contacts";
		
		// Phone Number
		File expectedMessage1 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute1.json").getPath());
		assertNotNull(expectedMessage1);
		File actualMessage1 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-140b4bbd-259e-3bac-86b9-9bc74dbff674");
		assertEquals(mapper.readTree(expectedMessage1), mapper.readTree(actualMessage1));

		// Person Name
		File expectedMessage2 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute2.json").getPath());
		assertNotNull(expectedMessage2);
		File acutalMessage2 = new File("data/outbox/" + 
		    "org.openmrs.sync.component.model.PersonAttributeModel-faadc59c-b240-3caa-882a-db61c614b314");
		assertEquals(mapper.readTree(expectedMessage2), mapper.readTree(acutalMessage2));

		// Person Name
		File expectedMessage3 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute3.json").getPath());
		assertNotNull(expectedMessage3);
		File actualMessage3 = new File("data/outbox/" + 
		    "org.openmrs.sync.component.model.PersonAttributeModel-5944ab2e-9cc1-307e-897d-307949892e71");
		assertEquals(mapper.readTree(expectedMessage3), mapper.readTree(actualMessage3));

	}

	@Test
	public void shouldHandlePatients() throws Exception {

		String patients = context.getTypeConverter().convertTo(
				String.class, new File("src/test/resources/h2-patients-10.json"));

		template.sendBodyAndHeader("file://data/inbox", patients,
				Exchange.FILE_NAME, "patients.json");

		Thread.sleep(2000);

		String expectedOutputFolder = OUTPUT_RESOURCES_FOLDER + "/Patients";
		
        ObjectMapper mapper = new ObjectMapper(); 

        // Patient
        File expectedMessage1 = new File(getClass().getResource(expectedOutputFolder + "/expectedPatient1.json").getPath());
		assertNotNull(expectedMessage1);
		File actualMessage1 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PatientModel-05eeac8d-100e-3fd7-a258-a33a663661c1");
		assertEquals(mapper.readTree(expectedMessage1), mapper.readTree(actualMessage1));
		
        // PersonName
		File expectedMessage2 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonName1.json").getPath());
		assertNotNull(expectedMessage2);
		File actualMessage2 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonNameModel-9f8ee9e2-f855-3040-95a0-a16b82fbf36b");
		assertEquals(mapper.readTree(expectedMessage2), mapper.readTree(actualMessage2));

		// PersonAttribute
		File expectedMessage3 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute1.json").getPath());
		assertNotNull(expectedMessage3);	
		File actualMessage3 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonAttributeModel-6dac6000-1f0f-3c00-8930-2c02a0ff72de");
		assertEquals(mapper.readTree(expectedMessage3), mapper.readTree(actualMessage3));

	}

}
