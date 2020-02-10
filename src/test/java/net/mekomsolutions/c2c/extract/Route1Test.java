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
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute1.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" + 
					"org.openmrs.sync.component.model.PersonAttributeModel-e1e18f6f-e681-3eb1-bd91-0072ee7f7a36");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Person Name
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute2.json").getPath());
			assertNotNull(expectedMessage);
			File acutalMessage2 = new File("data/outbox/" + 
					"org.openmrs.sync.component.model.PersonAttributeModel-77e70fb4-73cd-3c85-bcab-6ea8814de1d2");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(acutalMessage2));
		}
		// Person Name
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute3.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" + 
					"org.openmrs.sync.component.model.PersonAttributeModel-a9110597-45c5-3827-a3e6-db64ecb04708");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Contact Type
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute4.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" + 
					"org.openmrs.sync.component.model.PersonAttributeModel-dd199660-acf9-3093-9f9c-997d5fb9c9af");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Contact Person Relationship
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute5.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" + 
					"org.openmrs.sync.component.model.PersonAttributeModel-e19bc490-d02e-37d8-9c7b-6157f85977a4");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Contact Person Address
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute6.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" + 
					"org.openmrs.sync.component.model.PersonAttributeModel-98cf2b12-3ad9-38ad-bc6f-7a954affaa09");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
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
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPatient1.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PatientModel-05eeac8d-100e-3fd7-a258-a33a663661c1");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// PersonName
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonName1.json").getPath());
			assertNotNull(expectedMessage);
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PersonNameModel-9f8ee9e2-f855-3040-95a0-a16b82fbf36b");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Person Attribute: Phone Number
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute1.json").getPath());
			assertNotNull(expectedMessage);	
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PersonAttributeModel-fbf38a30-cfac-3f86-9f4f-ba2ba9e89767");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Person Attribute: Marital Status
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute2.json").getPath());
			assertNotNull(expectedMessage);	
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PersonAttributeModel-f31c3e84-fa85-3e7c-83da-e54e9cd2bba2");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Person Attribute: Employment
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPersonAttribute3.json").getPath());
			assertNotNull(expectedMessage);	
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PersonAttributeModel-e1311a90-8f91-3d1f-8583-d229d5400b21");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
		// Patient Identifier: Dossier Number
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPatientIdentifier1.json").getPath());
			assertNotNull(expectedMessage);	
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PatientIdentifierModel-fefe6836-281f-332c-ae92-724c4f7f3f84");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}		

		// Patient Identifier: VecnaID
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPatientIdentifier2.json").getPath());
			assertNotNull(expectedMessage);	
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PatientIdentifierModel-1b76e6d9-f9c8-3320-a5f6-458b108dd0a5");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}

		// Patient Identifier: VecnaGUID
		{
			File expectedMessage = new File(getClass().getResource(expectedOutputFolder + "/expectedPatientIdentifier3.json").getPath());
			assertNotNull(expectedMessage);	
			File actualMessage = new File("data/outbox/" +
					"org.openmrs.sync.component.model.PatientIdentifierModel-fd1b1031-acd7-31c5-a47c-e1a9458ec3da");
			assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
		}
	}

}
