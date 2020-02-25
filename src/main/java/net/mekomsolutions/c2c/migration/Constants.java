package net.mekomsolutions.c2c.migration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mekomsolutions.c2c.migration.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPatientIdentifier;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPerson;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonAddress;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonAttribute;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonName;

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

	public static final String JMS_COUCHBASE_QUEUE = "c2c.couchbase";

	private static Map<String, String> createFull() {

		Map<String, String> result = new HashMap<>();
		result.put(SyncPerson.class.getSimpleName(), "org.openmrs.sync.component.model.PersonModel");
		result.put(SyncPersonName.class.getSimpleName(), "org.openmrs.sync.component.model.PersonNameModel");
		result.put(SyncPatient.class.getSimpleName(), "org.openmrs.sync.component.model.PatientModel");
		result.put(SyncPersonAttribute.class.getSimpleName(), "org.openmrs.sync.component.model.PersonAttributeModel");
		result.put(SyncPatientIdentifier.class.getSimpleName(), "org.openmrs.sync.component.model.PatientIdentifierModel");
		result.put(SyncPersonAddress.class.getSimpleName(), "org.openmrs.sync.component.model.PersonAddressModel");

		return Collections.unmodifiableMap(result);
	}

	private static Map<String, String> createLight() {

		Map<String, String> result = new HashMap<>();
		result.put("Patient", "org.openmrs.sync.component.entity.light.PatientLight");
		result.put("User", "org.openmrs.sync.component.entity.light.UserLight");
		result.put("PersonAttributeType", "org.openmrs.sync.component.entity.light.PersonAttributeTypeLight");
		result.put("PatientIdentifierType", "org.openmrs.sync.component.entity.light.PatientIdentifierTypeLight");

		return Collections.unmodifiableMap(result);
	}

}