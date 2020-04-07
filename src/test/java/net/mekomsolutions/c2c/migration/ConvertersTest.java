package net.mekomsolutions.c2c.migration;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

import net.mekomsolutions.c2c.migration.entity.Contact;
import net.mekomsolutions.c2c.migration.entity.Diagnosis;
import net.mekomsolutions.c2c.migration.entity.EntityWrapper;
import net.mekomsolutions.c2c.migration.entity.Patient;
import net.mekomsolutions.c2c.migration.entity.Visit;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEncounter;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPatientIdentifier;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonAttribute;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonName;
import net.mekomsolutions.c2c.migration.entity.sync.SyncVisit;

public class ConvertersTest extends CamelTestSupport {

	private static final String COUCHBASE_SELECTS = "/couchbase_selects";

	private MockEndpoint mockVisits;
	private MockEndpoint mockDiagnoses;
	private MockEndpoint mockPatients;
	private MockEndpoint mockContacts;

	private UUID visitUuid;
	private UUID patientUuid;

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

				from("seda:queue:c2c-patient")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(Patient.class)
				.split(simple("${body.entities}"))
				.log("${body.modelClass}")
				.to("mock:patient-messages");

				from("seda:queue:c2c-contact")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(Contact.class)
				.split(simple("${body.entities}"))
				.log("${body.modelClass}")
				.to("mock:contact-messages");

				from("seda:queue:c2c-visit")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(Visit.class)
				.split(simple("${body.entities}"))
				.log("${body.modelClass}")
				.to("mock:visit-messages");

				from("seda:queue:c2c-diagnosis")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(Diagnosis.class)
				.split(simple("${body.entities}"))
				.log("${body.modelClass}")
				.to("mock:diagnosis-messages");
			}
		};
	}

	@Before
	public void loadABunchOfObjects() throws InterruptedException {

		mockPatients = getMockEndpoint("mock:patient-messages");
		mockContacts = getMockEndpoint("mock:contact-messages");
		mockVisits = getMockEndpoint("mock:visit-messages");
		mockDiagnoses = getMockEndpoint("mock:diagnosis-messages");

		// Load a patient
		template.sendBodyAndHeader("seda:queue:c2c-patient", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~patient/pat!~00~H3-1390cli~H3.json")
						.getFile())), Exchange.FILE_NAME, "pat!~00~H3-1390cli~H3.json");

		// Load her contact
		template.sendBodyAndHeader("seda:queue:c2c-contact", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~contact/con!~00~1~H3-1390cli~H3.json")
						.getFile())), Exchange.FILE_NAME, "con!~00~1~H3-1390cli~H3.json");

		// Load her visit
		template.sendBodyAndHeader("seda:queue:c2c-visit", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~visit/vst!~00~10000040cli~H3.json")
						.getFile())), Exchange.FILE_NAME, "vst!~00~10000040cli~H3.json");

		// Load a diagnosis for that visit
		template.sendBodyAndHeader("seda:queue:c2c-diagnosis", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~diagnosis/dia!~00~TwcAAAAAAAA~LMs.json")
						.getFile())), Exchange.FILE_NAME, "dia!~00~TwcAAAAAAAA~LMs.json");

		// Load the chief complaint for that visit
		template.sendBodyAndHeader("seda:queue:c2c-diagnosis", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~diagnosis/dia!~00~TwcAAAAAAAA~K8s.json")
						.getFile())), Exchange.FILE_NAME, "dia!~00~TwcAAAAAAAA~K8s.json");

		mockPatients.expectedMessageCount(8);
		mockPatients.assertIsSatisfied(); 

		mockContacts.expectedMessageCount(5);
		mockContacts.assertIsSatisfied();

		mockVisits.expectedMessageCount(6);
		mockVisits.assertIsSatisfied(); 

		mockDiagnoses.expectedMessageCount(5);
		mockDiagnoses.assertIsSatisfied();

		visitUuid = UUID.nameUUIDFromBytes("vst!~00~10000040cli~H3".getBytes());
		patientUuid = UUID.nameUUIDFromBytes("pat!~00~H3-1390cli~H3".getBytes());

	}

	@Test
	public void shouldConvertPatients() throws Exception {

		List<Exchange> patientMessages = mockPatients.getReceivedExchanges();

		EntityWrapper<?> body0 = patientMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncPatient patient = (SyncPatient) body0.getEntity();
		assertTrue(patient.getUuid().equals(patientUuid.toString()));
		assertTrue(patient.getBirthdate().equals(Utils.convertBirthdate("1989-03-18T00:00:00Z")));
		assertTrue(patient.getGender().equals("Female"));

		EntityWrapper<?> body1 = patientMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncPersonName personName = (SyncPersonName) body1.getEntity();
		assertTrue(personName.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(personName.getGivenName().equals("Wideline"));
		assertTrue(personName.getMiddleName().equals(""));
		assertTrue(personName.getFamilyName().equals("Pierre Louis"));

		EntityWrapper<?> body2 = patientMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute phoneNumber = (SyncPersonAttribute) body2.getEntity();
		assertTrue(phoneNumber.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(phoneNumber.getValue().equals("987423 654"));
		assertTrue(phoneNumber.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.phoneNumber.uuid}}")))));

		EntityWrapper<?> body3 = patientMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute maritalStatus = (SyncPersonAttribute) body3.getEntity();
		assertTrue(maritalStatus.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(maritalStatus.getValue().equals("Married"));
		assertTrue(maritalStatus.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.maritalStatus.uuid}}")))));

		EntityWrapper<?> body4 = patientMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute employment = (SyncPersonAttribute) body4.getEntity();
		assertTrue(employment.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(employment.getValue().equals("Commerçant"));
		assertTrue(employment.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.employment.uuid}}")))));

		EntityWrapper<?> body5 = patientMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier dossierNumber = (SyncPatientIdentifier) body5.getEntity();
		assertTrue(dossierNumber.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(dossierNumber.getIdentifier().equals("H3-1390"));
		assertTrue(dossierNumber.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pit.dossierNumber.uuid}}")))));

		EntityWrapper<?> body6 = patientMessages.get(6).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier vecnaId = (SyncPatientIdentifier) body6.getEntity();
		assertTrue(vecnaId.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(vecnaId.getIdentifier().equals("45459"));
		assertTrue(vecnaId.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pit.vecnaId.uuid}}")))));

		EntityWrapper<?> body7 = patientMessages.get(7).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier vecnaGuid = (SyncPatientIdentifier) body7.getEntity();
		assertTrue(vecnaGuid.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(vecnaGuid.getIdentifier().equals("98342BC4-CB52-498C-AAE5-30A19B0A9720"));
		assertTrue(vecnaGuid.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pit.vecnaGuid.uuid}}")))));

	}

	@Test
	public void shouldConvertContacts() throws Exception {

		List<Exchange> contactMessages = mockContacts.getReceivedExchanges();

		EntityWrapper<?> body0 = contactMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonName = (SyncPersonAttribute) body0.getEntity();
		assertTrue(contactPersonName.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonName.getValue().equals(Utils.concatName(new LinkedList<String>(
				Arrays.asList("Anacilen","Elvaris")))));
		assertTrue(contactPersonName.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.contactPersonName.uuid}}")))));

		EntityWrapper<?> body1 = contactMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonPhoneNumber = (SyncPersonAttribute) body1.getEntity();
		assertTrue(contactPersonPhoneNumber.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonPhoneNumber.getValue().equals(Utils.concatPhoneNumber(new LinkedList<String>(
				Arrays.asList("3-873-8041", "01234")))));
		assertTrue(contactPersonPhoneNumber.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.contactPersonPhone.uuid}}")))));

		EntityWrapper<?> body2 = contactMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonType = (SyncPersonAttribute) body2.getEntity();
		assertTrue(contactPersonType.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonType.getValue().equals("Emergency Contact"));
		assertTrue(contactPersonType.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.contactContactType.uuid}}")))));

		EntityWrapper<?> body3 = contactMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonRelationship = (SyncPersonAttribute) body3.getEntity();
		assertTrue(contactPersonRelationship.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonRelationship.getValue().equals("Son Mari"));
		assertTrue(contactPersonRelationship.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.contactRelationship.uuid}}")))));

		EntityWrapper<?> body4 = contactMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonAddress = (SyncPersonAttribute) body4.getEntity();
		assertTrue(contactPersonAddress.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonAddress.getValue().equals(Utils.concatAddresses(new LinkedList<String>(
				Arrays.asList("Camp Louis", "")))));
		assertTrue(contactPersonAddress.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", UUID.fromString(context().
						resolvePropertyPlaceholders("{{pat.contactPersonAddress.uuid}}")))));

	}

	@Test
	public void shouldConvertVisits() throws Exception {

		List<Exchange> visitMessages = mockVisits.getReceivedExchanges();

		EntityWrapper<?> body0 = visitMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncVisit visit = (SyncVisit) body0.getEntity();
		assertTrue(visit.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(visit.getDateStarted().equals(Utils.dateStringToArray("2017-07-28T08:48:17.429Z")));
		assertTrue(visit.getDateStopped().equals(Utils.dateStringToArray("2017-07-28T10:05:15.358Z")));

		EntityWrapper<?> body1 = visitMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncEncounter encounter = (SyncEncounter) body1.getEntity();
		assertTrue(encounter.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(encounter.getEncounterType().equals(Utils.getModelClassLight("EncounterType", UUID.fromString(context().
				resolvePropertyPlaceholders("{{encounterType.consultation.uuid}}")))));
		assertTrue(encounter.getEncounterDatetime().equals(Utils.dateStringToArray("2017-07-28T08:48:17.429Z")));
		assertTrue(encounter.getVisit().equals(Utils.getModelClassLight("Visit", visitUuid)));

		EntityWrapper<?> body2 = visitMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncObservation temperature = (SyncObservation) body2.getEntity();
		assertTrue(temperature.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(temperature.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.temperature.uuid}}")))));
		assertTrue(temperature.getObsDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> body3 = visitMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncObservation weight = (SyncObservation) body3.getEntity();
		assertTrue(weight.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(weight.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.weight.uuid}}")))));
		assertTrue(weight.getObsDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> body4 = visitMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation bpdiastolic = (SyncObservation) body4.getEntity();
		assertTrue(bpdiastolic.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(bpdiastolic.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.bpdiastolic.uuid}}")))));
		assertTrue(bpdiastolic.getObsDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> body5 = visitMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncObservation bpsystolic = (SyncObservation) body5.getEntity();
		assertTrue(bpsystolic.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(bpsystolic.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.bpsystolic.uuid}}")))));
		assertTrue(bpsystolic.getObsDatetime().equals(visit.getDateStarted()));

	}

	@Test
	public void shouldConvertDiagnoses() throws Exception {

		List<Exchange> diagnosisMessages = mockDiagnoses.getReceivedExchanges();

		EntityWrapper<?> body0 = diagnosisMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation visitDiag = (SyncObservation) body0.getEntity();
		assertTrue(visitDiag.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(visitDiag.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.visitDiagnoses.uuid}}")))));

		EntityWrapper<?> body1 = diagnosisMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncObservation nonCodedDiag = (SyncObservation) body1.getEntity();
		assertTrue(nonCodedDiag.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(nonCodedDiag.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.nonCodedDiagnosis.uuid}}")))));

		EntityWrapper<?> body2 = diagnosisMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncObservation diagnosisCertainty = (SyncObservation) body2.getEntity();
		assertTrue(diagnosisCertainty.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(diagnosisCertainty.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.diagnosisCertainty.uuid}}")))));

		EntityWrapper<?> body3 = diagnosisMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncObservation diagnosisOrder = (SyncObservation) body3.getEntity();
		assertTrue(diagnosisOrder.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(diagnosisOrder.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.diagnosisOrder.uuid}}")))));

		EntityWrapper<?> body4 = diagnosisMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaint = (SyncObservation) body4.getEntity();
		assertTrue(chiefComplaint.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(chiefComplaint.getConcept().equals(Utils.getModelClassLight("Concept", UUID.fromString(context().
				resolvePropertyPlaceholders("{{concept.chiefcomplaint.uuid}}")))));

	}

	@Test
	public void visitAndObsShouldShareTheSameEncounter() throws Exception {

		List<Exchange> visitMessages = mockVisits.getReceivedExchanges();

		EntityWrapper<?> vBody1 = visitMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncEncounter encounter = (SyncEncounter) vBody1.getEntity();
		EntityWrapper<?> vBody0 = visitMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncVisit visit = (SyncVisit) vBody0.getEntity();

		// Ensure the encounter has the correct reference to the visit
		String visitLight = Utils.getModelClassLight("Visit", visitUuid);
		assertTrue(encounter.getVisit().equals(visitLight));

		String encounterLight = Utils.getModelClassLight("Encounter",
				UUID.fromString(encounter.getUuid()));
		// Ensure the encounter has the correct date
		assertTrue(encounter.getEncounterDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> dBody0 = mockDiagnoses.getReceivedExchanges().get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation visitDiagnosis = (SyncObservation) dBody0.getEntity();
		// Ensure the diagnosis has the correct encounter
		assertTrue(visitDiagnosis.getEncounter().equals(encounterLight));

		EntityWrapper<?> dBody4 = mockDiagnoses.getReceivedExchanges().get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaint = (SyncObservation) dBody4.getEntity();
		// Ensure the chief complaint has the correct encounter
		assertTrue(chiefComplaint.getEncounter().equals(encounterLight));

	}

}
