package net.mekomsolutions.c2c.migration;

import java.io.File;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mekomsolutions.c2c.migration.route.CouchbaseToOpenMRSRoute;

public class CouchbaseToOpenMRSRouteTest extends CamelSpringTestSupport {

	private static final String EXPECTED_OUTPUT_RESOURCES_FOLDER = "/expected_output/";
	private static final String COUCHBASE_SELECTS = "/couchbase_selects/";

	@Rule
	public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

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

		// Setup the JMS VM
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactoryConfigurer("vm://embedded-broker?create=false").configure();
		context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		return context;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new CouchbaseToOpenMRSRoute();
	}

	@Test
	public void shouldProcessContactsAndOutputFiles() throws Exception {

		String contactsDirectoryName = "src/test/resources/couchbase_selects/dlm~00~c2c~contact/";
		File contactsDirectory = new File(contactsDirectoryName);

		// Dynamically load all files from the directory
		for (String fileName : contactsDirectory.list()) {
			String contacts = context.getTypeConverter().convertTo(
					String.class, new File(contactsDirectoryName + fileName));
			template.sendBodyAndHeader("jms:" + Constants.JMS_COUCHBASE_QUEUE,
					contacts, Exchange.FILE_NAME, fileName);
		}

		Thread.sleep(2000);

		ObjectMapper mapper = new ObjectMapper();

		String expectedFilesFolder = EXPECTED_OUTPUT_RESOURCES_FOLDER + "Contacts/";
		File expectedMessage = new File(getClass().getResource(expectedFilesFolder + "expectedPersonAttribute1.json").getPath());
		assertNotNull(expectedMessage);
		File actualMessage = new File("data/outbox/" + 
				"org.openmrs.sync.component.model.PersonAttributeModel-6e8d20d3-d915-3d32-8e95-d94afb6fcab1");
		assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));

	}

	@Test
	public void shouldProcessPatientsAndOutputFiles() throws Exception {

		String patientDirectoryName = "src/test/resources/couchbase_selects/dlm~00~c2c~patient/";
		File patientsDirectory = new File(patientDirectoryName);

		// Dynamically load all files from the directory
		for (String fileName : patientsDirectory.list()) {
			String contacts = context.getTypeConverter().convertTo(
					String.class, new File(patientDirectoryName + fileName));
			template.sendBodyAndHeader("jms:" + Constants.JMS_COUCHBASE_QUEUE,
					contacts, Exchange.FILE_NAME, fileName);
		}

		Thread.sleep(2000);
		String expectedFilesFolder = EXPECTED_OUTPUT_RESOURCES_FOLDER + "/Patients";
		ObjectMapper mapper = new ObjectMapper(); 

		File expectedMessage = new File(getClass().getResource(expectedFilesFolder + "/expectedPatient1.json").getPath());
		assertNotNull(expectedMessage);
		File actualMessage = new File("data/outbox/" +
				"org.openmrs.sync.component.model.PatientModel-05eeac8d-100e-3fd7-a258-a33a663661c1");
		assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
	}

	@Test
	public void shouldProcessVisitsAndOutputFiles() throws Exception {

		String patientDirectoryName = "src/test/resources/couchbase_selects/dlm~00~c2c~visit/";
		File patientsDirectory = new File(patientDirectoryName);

		// Dynamically load all files from the directory
		for (String fileName : patientsDirectory.list()) {
			String contacts = context.getTypeConverter().convertTo(
					String.class, new File(patientDirectoryName + fileName));
			template.sendBodyAndHeader("jms:" + Constants.JMS_COUCHBASE_QUEUE,
					contacts, Exchange.FILE_NAME, fileName);
		}

		Thread.sleep(2000);

		String expectedFilesFolder = EXPECTED_OUTPUT_RESOURCES_FOLDER + "/Visits";

		ObjectMapper mapper = new ObjectMapper(); 
		File expectedMessage = new File(getClass().getResource(expectedFilesFolder + "/expectedVisit1.json").getPath());
		assertNotNull(expectedMessage);
		File actualMessage = new File("data/outbox/" +
				"org.openmrs.sync.component.model.VisitModel-f21f43e6-722d-3239-8251-f02daab71515");
		assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));

	}

	@Test
	public void shouldProcessDiagnosesAndOutputFiles() throws Exception {

		String diagnosisDirectoryName = "src/test/resources/couchbase_selects/dlm~00~c2c~diagnosis/";
		File diagnosisDirectory = new File(diagnosisDirectoryName);

		// Dynamically load all files from the directory
		for (String fileName : diagnosisDirectory.list()) {
			String contacts = context.getTypeConverter().convertTo(
					String.class, new File(diagnosisDirectoryName + fileName));
			template.sendBodyAndHeader("jms:" + Constants.JMS_COUCHBASE_QUEUE,
					contacts, Exchange.FILE_NAME, fileName);
		}

		Thread.sleep(2000);

		String expectedFilesFolder = EXPECTED_OUTPUT_RESOURCES_FOLDER + "/Diagnoses";

		ObjectMapper mapper = new ObjectMapper(); 
		File expectedMessage = new File(getClass().getResource(expectedFilesFolder + "/expectedObservation1.json").getPath());
		assertNotNull(expectedMessage);
		File actualMessage = new File("data/outbox/" +
				"org.openmrs.sync.component.model.ObservationModel-a559fa3c-2bc5-39f7-9e54-7e9578214b99");
		assertEquals(mapper.readTree(expectedMessage), mapper.readTree(actualMessage));
	}

}
