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
import org.junit.Ignore;
import org.junit.Test;

import net.mekomsolutions.c2c.migration.entity.Contact;
import net.mekomsolutions.c2c.migration.entity.Diagnosis;
import net.mekomsolutions.c2c.migration.entity.EntityWrapper;
import net.mekomsolutions.c2c.migration.entity.LabTest;
import net.mekomsolutions.c2c.migration.entity.MedicineEvent;
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
	private MockEndpoint mockMedicineEvents;
	private MockEndpoint mockLabTests;

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

				from("seda:queue:c2c-medicineevent")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(MedicineEvent.class)
				.split(simple("${body.entities}"))
				.log("${body.modelClass}")
				.to("mock:medicineevent-messages");

				from("seda:queue:c2c-labtest")
				.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
				.convertBodyTo(LabTest.class)
				.split(simple("${body.entities}"))
				.log("${body.modelClass}")
				.to("mock:labtest-messages");
			}
		};
	}

	@Before
	public void loadABunchOfObjects() throws InterruptedException {

		mockPatients = getMockEndpoint("mock:patient-messages");
		mockContacts = getMockEndpoint("mock:contact-messages");
		mockVisits = getMockEndpoint("mock:visit-messages");
		mockDiagnoses = getMockEndpoint("mock:diagnosis-messages");
		mockMedicineEvents = getMockEndpoint("mock:medicineevent-messages");
		mockLabTests = getMockEndpoint("mock:labtest-messages");

		// Load a patient
		template.sendBodyAndHeader("seda:queue:c2c-patient", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~patient/pat!~00~H3-1390cli~H3.json")
						.getFile())), Exchange.FILE_NAME, "pat!~00~H3-1390cli~H3.json");

		// Load his contact
		template.sendBodyAndHeader("seda:queue:c2c-contact", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~contact/con!~00~1~H3-1390cli~H3.json")
						.getFile())), Exchange.FILE_NAME, "con!~00~1~H3-1390cli~H3.json");

		// Load his visit
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

		// Load a lab test *NOT FOR THAT VISIT*, but still for that patient
		template.sendBodyAndHeader("seda:queue:c2c-labtest", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~labtest/lae!~00~UQcAAAAAAAA~pZM.json")
						.getFile())), Exchange.FILE_NAME, "lae!~00~UQcAAAAAAAA~pZM.json");

		// Load a medicine event for that visit
		template.sendBodyAndHeader("seda:queue:c2c-medicineevent", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~medicineevent/mee!~00~UAcAAAAAAAA~K6I.json")
						.getFile())), Exchange.FILE_NAME, "mee!~00~UAcAAAAAAAA~K6.json");
		// and another one with different fields
		template.sendBodyAndHeader("seda:queue:c2c-medicineevent", context.getTypeConverter().convertTo(
				String.class, new File(getClass()
						.getResource(COUCHBASE_SELECTS + "/dlm~00~c2c~medicineevent/mee!~00~RwcAAAAAAAA~-04.json")
						.getFile())), Exchange.FILE_NAME, "mee!~00~RwcAAAAAAAA~-04.json");


		mockPatients.expectedMessageCount(9);
		mockPatients.assertIsSatisfied(); 

		mockContacts.expectedMessageCount(5);
		mockContacts.assertIsSatisfied();

		mockVisits.expectedMessageCount(7);
		mockVisits.assertIsSatisfied(); 

		mockDiagnoses.expectedMessageCount(9);
		mockDiagnoses.assertIsSatisfied();

		mockLabTests.expectedMessageCount(6);
		mockLabTests.assertIsSatisfied();

		mockMedicineEvents.expectedMessageCount(13);
		mockMedicineEvents.assertIsSatisfied();

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
		assertTrue(patient.getGender().equals(context().
				resolvePropertyPlaceholders("{{gender.female}}")));

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
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.phoneNumber.uuid}}"))));

		EntityWrapper<?> body3 = patientMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute maritalStatus = (SyncPersonAttribute) body3.getEntity();
		assertTrue(maritalStatus.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(maritalStatus.getValue().equals("Married"));
		assertTrue(maritalStatus.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.maritalStatus.uuid}}"))));

		EntityWrapper<?> body4 = patientMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute employment = (SyncPersonAttribute) body4.getEntity();
		assertTrue(employment.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(employment.getValue().equals("Commerçant"));
		assertTrue(employment.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.employment.uuid}}"))));

		EntityWrapper<?> body5 = patientMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier numeroDossier = (SyncPatientIdentifier) body5.getEntity();
		assertTrue(numeroDossier.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(numeroDossier.getIdentifier().equals("0H3-1390"));
		assertTrue(numeroDossier.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", context().
						resolvePropertyPlaceholders("{{pit.numeroDossier.uuid}}"))));

		EntityWrapper<?> body6 = patientMessages.get(6).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier ancienNumeroDossier = (SyncPatientIdentifier) body6.getEntity();
		assertTrue(ancienNumeroDossier.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(ancienNumeroDossier.getIdentifier().equals("H3-1390"));
		assertTrue(ancienNumeroDossier.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", context().
						resolvePropertyPlaceholders("{{pit.ancienNumeroDossier.uuid}}"))));

		EntityWrapper<?> body7 = patientMessages.get(7).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier vecnaId = (SyncPatientIdentifier) body7.getEntity();
		assertTrue(vecnaId.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(vecnaId.getIdentifier().equals("45459"));
		assertTrue(vecnaId.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", context().
						resolvePropertyPlaceholders("{{pit.vecnaId.uuid}}"))));

		EntityWrapper<?> body8 = patientMessages.get(8).getIn().getBody(EntityWrapper.class);
		SyncPatientIdentifier vecnaGuid = (SyncPatientIdentifier) body8.getEntity();
		assertTrue(vecnaGuid.getPatient().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(vecnaGuid.getIdentifier().equals("98342BC4-CB52-498C-AAE5-30A19B0A9720"));
		assertTrue(vecnaGuid.getPatientIdentifierType().equals(
				Utils.getModelClassLight("PatientIdentifierType", context().
						resolvePropertyPlaceholders("{{pit.vecnaGuid.uuid}}"))));

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
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.contactPersonName.uuid}}"))));

		EntityWrapper<?> body1 = contactMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonPhoneNumber = (SyncPersonAttribute) body1.getEntity();
		assertTrue(contactPersonPhoneNumber.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonPhoneNumber.getValue().equals(Utils.concatPhoneNumber(new LinkedList<String>(
				Arrays.asList("3-873-8041", "01234")))));
		assertTrue(contactPersonPhoneNumber.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.contactPersonPhone.uuid}}"))));

		EntityWrapper<?> body2 = contactMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonType = (SyncPersonAttribute) body2.getEntity();
		assertTrue(contactPersonType.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonType.getValue().equals("Emergency Contact"));
		assertTrue(contactPersonType.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.contactContactType.uuid}}"))));

		EntityWrapper<?> body3 = contactMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonRelationship = (SyncPersonAttribute) body3.getEntity();
		assertTrue(contactPersonRelationship.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonRelationship.getValue().equals("Son Mari"));
		assertTrue(contactPersonRelationship.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.contactRelationship.uuid}}"))));

		EntityWrapper<?> body4 = contactMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncPersonAttribute contactPersonAddress = (SyncPersonAttribute) body4.getEntity();
		assertTrue(contactPersonAddress.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(contactPersonAddress.getValue().equals(Utils.concatAddresses(new LinkedList<String>(
				Arrays.asList("Camp Louis", "")))));
		assertTrue(contactPersonAddress.getPersonAttributeType().equals(
				Utils.getModelClassLight("PersonAttributeType", context().
						resolvePropertyPlaceholders("{{pat.contactPersonAddress.uuid}}"))));

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
		assertTrue(encounter.getEncounterType().equals(Utils.getModelClassLight("EncounterType", context().
				resolvePropertyPlaceholders("{{encounterType.consultation.uuid}}"))));
		assertTrue(encounter.getEncounterDatetime().equals(Utils.dateStringToArray("2017-07-28T08:48:17.429Z")));
		assertTrue(encounter.getVisit().equals(Utils.getModelClassLight("Visit", visitUuid)));

		EntityWrapper<?> body2 = visitMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncObservation temperature = (SyncObservation) body2.getEntity();
		assertTrue(temperature.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(temperature.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.temperature.uuid}}"))));
		assertTrue(temperature.getObsDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> body3 = visitMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncObservation weight = (SyncObservation) body3.getEntity();
		assertTrue(weight.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(weight.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.weight.uuid}}"))));
		assertTrue(weight.getObsDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> body4 = visitMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation rr = (SyncObservation) body4.getEntity();
		assertTrue(rr.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(rr.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.respiratoryrate.uuid}}"))));
		assertTrue(rr.getObsDatetime().equals(visit.getDateStarted()));
		
		EntityWrapper<?> body5 = visitMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncObservation bpdiastolic = (SyncObservation) body5.getEntity();
		assertTrue(bpdiastolic.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(bpdiastolic.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.bpdiastolic.uuid}}"))));
		assertTrue(bpdiastolic.getObsDatetime().equals(visit.getDateStarted()));

		EntityWrapper<?> body6 = visitMessages.get(6).getIn().getBody(EntityWrapper.class);
		SyncObservation bpsystolic = (SyncObservation) body6.getEntity();
		assertTrue(bpsystolic.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(bpsystolic.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.bpsystolic.uuid}}"))));
		assertTrue(bpsystolic.getObsDatetime().equals(visit.getDateStarted()));

	}

	@Test
	public void shouldConvertDiagnoses() throws Exception {

		List<Exchange> diagnosisMessages = mockDiagnoses.getReceivedExchanges();

		EntityWrapper<?> body0 = diagnosisMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation visitDiag = (SyncObservation) body0.getEntity();
		assertTrue(visitDiag.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(visitDiag.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.visitDiagnoses.uuid}}"))));

		EntityWrapper<?> body1 = diagnosisMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncObservation nonCodedDiag = (SyncObservation) body1.getEntity();
		assertTrue(nonCodedDiag.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(nonCodedDiag.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.nonCodedDiagnosis.uuid}}"))));

		EntityWrapper<?> body2 = diagnosisMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncObservation diagnosisCertainty = (SyncObservation) body2.getEntity();
		assertTrue(diagnosisCertainty.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(diagnosisCertainty.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.diagnosisCertainty.uuid}}"))));

		EntityWrapper<?> body3 = diagnosisMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncObservation diagnosisOrder = (SyncObservation) body3.getEntity();
		assertTrue(diagnosisOrder.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(diagnosisOrder.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.diagnosisOrder.uuid}}"))));

		EntityWrapper<?> body4 = diagnosisMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation diagnosisRevised = (SyncObservation) body4.getEntity();
		assertTrue(diagnosisRevised.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(diagnosisRevised.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.diagnosisRevised.uuid}}"))));
		assertTrue(diagnosisRevised.getValueCoded().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.no.uuid}}"))));
		
		EntityWrapper<?> body5 = diagnosisMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncObservation initialDiagnosis = (SyncObservation) body5.getEntity();
		assertTrue(initialDiagnosis.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(initialDiagnosis.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.initialDiagnosis.uuid}}"))));
		assertTrue(initialDiagnosis.getValueText().equals(visitDiag.getUuid()));
				
		EntityWrapper<?> body6 = diagnosisMessages.get(6).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaintData = (SyncObservation) body6.getEntity();
		assertTrue(chiefComplaintData.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(chiefComplaintData.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.chiefcomplaintData.uuid}}"))));
		
		EntityWrapper<?> body7 = diagnosisMessages.get(7).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaintCoded = (SyncObservation) body7.getEntity();
		assertTrue(chiefComplaintCoded.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(chiefComplaintCoded.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.chiefcomplaintCoded.uuid}}"))));
		assertTrue(chiefComplaintCoded.getValueCoded().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.chiefcomplaintCoded.other.uuid}}"))));
		
		EntityWrapper<?> body8 = diagnosisMessages.get(8).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaintDetails = (SyncObservation) body8.getEntity();
		assertTrue(chiefComplaintDetails.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(chiefComplaintDetails.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.chiefcomplaintDetails.uuid}}"))));

	}

	@Test
	public void shouldConvertLabTest() throws Exception {

		List<Exchange> labTestMessages = mockLabTests.getReceivedExchanges();

		EntityWrapper<?> body0 = labTestMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation obsgroup = (SyncObservation) body0.getEntity();
		assertTrue(obsgroup.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(obsgroup.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.labtest.uuid}}"))));

		EntityWrapper<?> body1 = labTestMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncObservation name = (SyncObservation) body1.getEntity();
		assertTrue(name.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(name.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.labtestName.uuid}}"))));

		EntityWrapper<?> body2 = labTestMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncObservation price = (SyncObservation) body2.getEntity();
		assertTrue(price.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(price.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.labtestPrice.uuid}}"))));

		EntityWrapper<?> body3 = labTestMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncObservation cost = (SyncObservation) body3.getEntity();
		assertTrue(cost.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(cost.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.labtestCost.uuid}}"))));

		EntityWrapper<?> body4 = labTestMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation dispensed = (SyncObservation) body4.getEntity();
		assertTrue(dispensed.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(dispensed.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.labtestDispensed.uuid}}"))));

		EntityWrapper<?> body5 = labTestMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncObservation prescribed = (SyncObservation) body5.getEntity();
		assertTrue(prescribed.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(prescribed.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.labtestPrescribed.uuid}}"))));

	}

	@Test
	public void shouldConvertMedicineEvent() throws Exception {

		List<Exchange> meeMessages = mockMedicineEvents.getReceivedExchanges();

		EntityWrapper<?> body0 = meeMessages.get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation obsgroup = (SyncObservation) body0.getEntity();
		assertTrue(obsgroup.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(obsgroup.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorder.uuid}}"))));

		EntityWrapper<?> body1 = meeMessages.get(1).getIn().getBody(EntityWrapper.class);
		SyncObservation name = (SyncObservation) body1.getEntity();
		assertTrue(name.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(name.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderName.uuid}}"))));
		assertTrue(name.getValueText().equals("CIP-01A CIPROX"));

		EntityWrapper<?> body2 = meeMessages.get(2).getIn().getBody(EntityWrapper.class);
		SyncObservation price = (SyncObservation) body2.getEntity();
		assertTrue(price.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(price.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderUnitPrice.uuid}}"))));
		assertTrue(price.getValueNumeric().equals("75"));

		EntityWrapper<?> body3 = meeMessages.get(3).getIn().getBody(EntityWrapper.class);
		SyncObservation cost = (SyncObservation) body3.getEntity();
		assertTrue(cost.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(cost.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderCost.uuid}}"))));
		assertTrue(cost.getValueNumeric().equals("60"));

		EntityWrapper<?> body4 = meeMessages.get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation dispensed = (SyncObservation) body4.getEntity();
		assertTrue(dispensed.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(dispensed.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderDispensed.uuid}}"))));
		assertTrue(dispensed.getValueNumeric().equals("1"));

		EntityWrapper<?> body5 = meeMessages.get(5).getIn().getBody(EntityWrapper.class);
		SyncObservation prescribed = (SyncObservation) body5.getEntity();
		assertTrue(prescribed.getPerson().equals(Utils.getModelClassLight("Patient", patientUuid)));
		assertTrue(prescribed.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderPrescribed.uuid}}"))));
		assertTrue(prescribed.getValueNumeric().equals("1"));
		
		EntityWrapper<?> body8 = meeMessages.get(8).getIn().getBody(EntityWrapper.class);
		SyncObservation totalprice = (SyncObservation) body8.getEntity();
		assertTrue(totalprice.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderTotalPrice.uuid}}"))));
		assertTrue(totalprice.getValueNumeric().equals("100"));

		EntityWrapper<?> body12 = meeMessages.get(12).getIn().getBody(EntityWrapper.class);
		SyncObservation reco = (SyncObservation) body12.getEntity();
		assertTrue(reco.getConcept().equals(Utils.getModelClassLight("Concept", context().
				resolvePropertyPlaceholders("{{concept.drugorderRecommendedDose.uuid}}"))));
		assertTrue(reco.getValueNumeric().equals("30"));
	}

	@Ignore
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

		// Diagnosis encounter
		EntityWrapper<?> dBody0 = mockDiagnoses.getReceivedExchanges().get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation visitDiagnosis = (SyncObservation) dBody0.getEntity();
		assertTrue(visitDiagnosis.getEncounter().equals(encounterLight));

		EntityWrapper<?> dBody4 = mockDiagnoses.getReceivedExchanges().get(4).getIn().getBody(EntityWrapper.class);
		SyncObservation chiefComplaint = (SyncObservation) dBody4.getEntity();
		assertTrue(chiefComplaint.getEncounter().equals(encounterLight));

		// Medicine Events encounter
		EntityWrapper<?> mBody0 = mockMedicineEvents.getReceivedExchanges().get(0).getIn().getBody(EntityWrapper.class);
		SyncObservation meeObsGroup = (SyncObservation) mBody0.getEntity();
		assertTrue(meeObsGroup.getEncounter().equals(encounterLight));

		EntityWrapper<?> mBody1 = mockMedicineEvents.getReceivedExchanges().get(1).getIn().getBody(EntityWrapper.class);
		SyncObservation meeName = (SyncObservation) mBody1.getEntity();
		assertTrue(meeName.getEncounter().equals(encounterLight));

	}

}
