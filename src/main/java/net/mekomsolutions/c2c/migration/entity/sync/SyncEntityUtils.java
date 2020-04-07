package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;

public class SyncEntityUtils {

	public static void createAndAddPersonAttribute(String personAttributeTypeProperty, String attributeValue, String patientRefKey, Map<String, String> data, Exchange exchange, List<SyncEntity> allEntities) throws Exception {
		if (Utils.hasKeyOrValue(attributeValue)) {

			SyncPersonAttribute pa = new SyncPersonAttribute(data, exchange);

			pa.setValue(attributeValue);

			String personAttributeTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{" + personAttributeTypeProperty + "}}");
			pa.setPersonAttributeType(Utils.getModelClassLight("PersonAttributeType", UUID.fromString(personAttributeTypeUuid)));

			pa.setPerson(Utils.getModelClassLight("Patient", UUID.nameUUIDFromBytes(data.get(patientRefKey).getBytes())));

			// Do not forget to override the UUID to a unique one.
			pa.setUuid(computeNewUUID(personAttributeTypeProperty, data.get(patientRefKey)));

			allEntities.add(pa);
		}
	}

	public static void createAndAddPatientIdentifier(String patientIdentifierTypeProperty, String identifierValue, String patientRefKey, boolean preferred, Map<String, String> data, Exchange exchange, List<SyncEntity> allEntities) throws Exception {
		if (Utils.hasKeyOrValue(identifierValue)) {

			SyncPatientIdentifier pi = new SyncPatientIdentifier(data, exchange);

			pi.setIdentifier(identifierValue);
			pi.setPreferred(preferred);
			String patientIdentifierTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{" + patientIdentifierTypeProperty + "}}");
			pi.setPatientIdentifierType(Utils.getModelClassLight("PatientIdentifierType", UUID.fromString(patientIdentifierTypeUuid)));

			pi.setPatient(Utils.getModelClassLight("Patient", UUID.nameUUIDFromBytes(data.get(patientRefKey).getBytes())));

			// Do not forget to override the UUID to a unique one.
			pi.setUuid(computeNewUUID(patientIdentifierTypeProperty, data.get(patientRefKey)));

			allEntities.add(pi);
		}
	}
	
	/**
	 * Hopefully computes a unique identifier based on 2 input strings. The first is the actual value of the entity.
	 * This may be the Identifier value, the Attribute value etc...
	 * The second argument is the UUID (as a String) of the type of entity being processed.
	 * Could be the PersonAttributeTypeUuid or PatientIdentifierTypeUuid for instance.
	 * 
	 * @param value The entity actual value used computation (entity.getValue(), entity.getIdentifier()...)
	 * @param entityTypeUuid UUID of the type of entity (PersonAttributeTypeUuid, PatientIdentifierTypeUuid...)
	 * @param data The Camel data
	 * 
	 * @return uuid a computed UUID as a String.
	 * 
	 */
	public static String computeNewUUID(String value, String entityTypeUuid, Map<String, String> data) {
		return UUID.nameUUIDFromBytes((entityTypeUuid +
				data.get(Constants.OBJECT_KEY) + value).getBytes()).toString();
	}
	/**
	 * An more flexible constructor for UUIDs that need to be generated and used across objects
	 * 
	 * @param entityTypeProperty Property name of the entity ("addressline1", "vecnaid"...)
	 * @param any reference, but likely a reference to the 'key' of a linked object. For instance the 'visitKey'
	 * of a Diagnosis object.
	 * 
	 * @return uuid a computed UUID as a String.
	 *
	 */
	public static String computeNewUUID(String entityTypeProperty, String ref) {
		return UUID.nameUUIDFromBytes(
				(entityTypeProperty + ref).getBytes()).toString();
	}
}
