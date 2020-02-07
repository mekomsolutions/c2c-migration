package net.mekomsolutions.c2c.extract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mekomsolutions.c2c.extract.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPerson;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonAddress;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonAttribute;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonName;

public class Constants {

	// TOOD: This should be brought via the application.properties file.
	/**
	 * Refers to an existing user
	 */
	public static final String USER_UUID = "8295e796-4016-11e4-8e9b-2939a1809c8e";

	public static final Map<String, String> FULL_MODEL_CLASS_NAMES = createFull();

	public static final Map<String, String> LIGHT_MODEL_CLASS_NAMES = createLight();

	private static Map<String, String> createFull() {

		Map<String, String> result = new HashMap<String, String>();
		result.put(SyncPerson.class.getSimpleName(), "org.openmrs.sync.component.model.PersonModel");
		result.put(SyncPersonName.class.getSimpleName(), "org.openmrs.sync.component.model.PersonNameModel");
		result.put(SyncPatient.class.getSimpleName(), "org.openmrs.sync.component.model.PatientModel");
		result.put(SyncPersonAttribute.class.getSimpleName(), "org.openmrs.sync.component.model.PersonAttributeModel");

		return Collections.unmodifiableMap(result);
	}

	private static Map<String, String> createLight() {

		Map<String, String> result = new HashMap<String, String>();
		result.put("Patient", "org.openmrs.sync.component.entity.light.PatientLight");
		result.put("User", "org.openmrs.sync.component.entity.light.UserLight");
		result.put("PersonAttributeType", "org.openmrs.sync.component.entity.light.PersonAttributeTypeLight");

		return Collections.unmodifiableMap(result);
	}

}