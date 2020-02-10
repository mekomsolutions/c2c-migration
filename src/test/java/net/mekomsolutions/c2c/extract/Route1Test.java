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

//	@Ignore
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
				"org.openmrs.sync.component.model.PersonAttributeModel-e1e18f6f-e681-3eb1-bd91-0072ee7f7a36");
		assertEquals(mapper.readTree(expectedMessage1), mapper.readTree(actualMessage1));

		// Person Name
		File expectedMessage2 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute2.json").getPath());
		assertNotNull(expectedMessage2);
		File acutalMessage2 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-77e70fb4-73cd-3c85-bcab-6ea8814de1d2");
		assertEquals(mapper.readTree(expectedMessage2), mapper.readTree(acutalMessage2));

		// Person Name
		File expectedMessage3 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute3.json").getPath());
		assertNotNull(expectedMessage3);
		File actualMessage3 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-a9110597-45c5-3827-a3e6-db64ecb04708");
		assertEquals(mapper.readTree(expectedMessage3), mapper.readTree(actualMessage3));

		// Contact Type
		File expectedMessage4 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute4.json").getPath());
		assertNotNull(expectedMessage4);
		File actualMessage4 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-dd199660-acf9-3093-9f9c-997d5fb9c9af");
		assertEquals(mapper.readTree(expectedMessage4), mapper.readTree(actualMessage4));

		// Contact Person Relationship
		File expectedMessage5 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute5.json").getPath());
		assertNotNull(expectedMessage5);
		File actualMessage5 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-e19bc490-d02e-37d8-9c7b-6157f85977a4");
		assertEquals(mapper.readTree(expectedMessage5), mapper.readTree(actualMessage5));

		// Contact Person Address
		File expectedMessage6 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute6.json").getPath());
		assertNotNull(expectedMessage6);
		File actualMessage6 = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-98cf2b12-3ad9-38ad-bc6f-7a954affaa09");
		assertEquals(mapper.readTree(expectedMessage6), mapper.readTree(actualMessage6));

	}

//	@Ignore
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

		// Person Attribute: Phone Number
		File expectedMessage3 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute1.json").getPath());
		assertNotNull(expectedMessage3);	
		File actualMessage3 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonAttributeModel-fbf38a30-cfac-3f86-9f4f-ba2ba9e89767");
		assertEquals(mapper.readTree(expectedMessage3), mapper.readTree(actualMessage3));

		// Person Attribute: Marital Status
		File expectedMessage4 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute2.json").getPath());
		assertNotNull(expectedMessage4);	
		File actualMessage4 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonAttributeModel-f31c3e84-fa85-3e7c-83da-e54e9cd2bba2");
		assertEquals(mapper.readTree(expectedMessage4), mapper.readTree(actualMessage4));
		
		// Person Attribute: Employment
		File expectedMessage5 = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute3.json").getPath());
		assertNotNull(expectedMessage5);	
		File actualMessage5 = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PersonAttributeModel-e1311a90-8f91-3d1f-8583-d229d5400b21");
		assertEquals(mapper.readTree(expectedMessage5), mapper.readTree(actualMessage5));
		
		// PIT: Dossier Number
		// PIT: VecnaID
		// PIT: VecnaQUID


	}

}
