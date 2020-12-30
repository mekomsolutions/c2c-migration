package net.mekomsolutions.c2c.migration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mekomsolutions.c2c.migration.entity.sync.SyncEncounter;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPatientIdentifier;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPerson;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonAddress;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonAttribute;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonName;
import net.mekomsolutions.c2c.migration.entity.sync.SyncVisit;

public class Constants {

	private Constants() {
	}

	// TODO: This should be brought via the application.properties file.
	/**
	 * Refers to an existing user
	 */
	public static final String USER_UUID = "8295e796-4016-11e4-8e9b-2939a1809c8e";

	protected static final Map<String, String> FULL_MODEL_CLASS_NAMES = createFull();

	protected static final Map<String, String> LIGHT_MODEL_CLASS_NAMES = createLight();

	public static final String OBJECT_KEY = "objKey";

	public static final String PATIENT_KEY = "patientKey";
	
	public static final String VISIT_KEY = "visitKey";

	public static final String JMS_COUCHBASE_QUEUE = "c2c.couchbase";

	private static Map<String, String> createFull() {

		Map<String, String> result = new HashMap<>();
		result.put(SyncPerson.class.getSimpleName(), "org.openmrs.sync.component.model.PersonModel");
		result.put(SyncPersonName.class.getSimpleName(), "org.openmrs.sync.component.model.PersonNameModel");
		result.put(SyncPatient.class.getSimpleName(), "org.openmrs.sync.component.model.PatientModel");
		result.put(SyncPersonAttribute.class.getSimpleName(), "org.openmrs.sync.component.model.PersonAttributeModel");
		result.put(SyncPatientIdentifier.class.getSimpleName(), "org.openmrs.sync.component.model.PatientIdentifierModel");
		result.put(SyncPersonAddress.class.getSimpleName(), "org.openmrs.sync.component.model.PersonAddressModel");
		result.put(SyncVisit.class.getSimpleName(), "org.openmrs.sync.component.model.VisitModel");
		result.put(SyncObservation.class.getSimpleName(), "org.openmrs.sync.component.model.ObservationModel");
		result.put(SyncEncounter.class.getSimpleName(), "org.openmrs.sync.component.model.EncounterModel");

		return Collections.unmodifiableMap(result);
	}

	private static Map<String, String> createLight() {

		Map<String, String> result = new HashMap<>();
		result.put("Patient", "org.openmrs.sync.component.entity.light.PatientLight");
		result.put("User", "org.openmrs.sync.component.entity.light.UserLight");
		result.put("PersonAttributeType", "org.openmrs.sync.component.entity.light.PersonAttributeTypeLight");
		result.put("PatientIdentifierType", "org.openmrs.sync.component.entity.light.PatientIdentifierTypeLight");
		result.put("Visit", "org.openmrs.sync.component.entity.light.VisitLight");
		result.put("VisitType", "org.openmrs.sync.component.entity.light.VisitTypeLight");
		result.put("Concept", "org.openmrs.sync.component.entity.light.ConceptLight");
		result.put("Encounter", "org.openmrs.sync.component.entity.light.EncounterLight");
		result.put("EncounterType", "org.openmrs.sync.component.entity.light.EncounterTypeLight");
		result.put("Location", "org.openmrs.sync.component.entity.light.LocationLight");
		result.put("Observation", "org.openmrs.sync.component.entity.light.ObservationLight");

		return Collections.unmodifiableMap(result);
	}

}