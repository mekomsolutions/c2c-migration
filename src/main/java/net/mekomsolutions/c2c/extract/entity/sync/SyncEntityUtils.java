package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.extract.Utils;

public class SyncEntityUtils {

	public static void createAndAddPersonAttribute(String personAttributeTypeProperty, String attributeValue, String patientRefKey, HashMap<String,String> data, Exchange exchange, List<SyncEntity> allEntities) throws Exception {
		if (Utils.hasKeyOrValue(attributeValue)) {

			SyncPersonAttribute pa = new SyncPersonAttribute(data, exchange);

			pa.setValue(attributeValue);

			String personAttributeTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{" + personAttributeTypeProperty + "}}");
			pa.setPersonAttributeType(Utils.getModelClassLight("PersonAttributeType", UUID.fromString(personAttributeTypeUuid)));

			pa.setPerson(Utils.getModelClassLight("Patient", UUID.nameUUIDFromBytes(data.get(patientRefKey).getBytes())));

			// Do not forget to override the UUID to a unique one.
			pa.computeNewUUID(personAttributeTypeUuid, data);

			allEntities.add(pa);
		}
	}

	public static void createAndAddPatientIdentifier(String patientIdentifierTypeProperty, String identifierValue, String patientRefKey, HashMap<String,String> data, Exchange exchange, List<SyncEntity> allEntities) throws Exception {
		if (Utils.hasKeyOrValue(identifierValue)) {
			
			SyncPatientIdentifier pi = new SyncPatientIdentifier(data, exchange);
			
			pi.setIdentifier(identifierValue);
			
			String patientIdentifierTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{" + patientIdentifierTypeProperty + "}}");
			pi.setPatientIdentifierType(Utils.getModelClassLight("PatientIdentifierType", UUID.fromString(patientIdentifierTypeUuid)));
			
			pi.setPatient(Utils.getModelClassLight("Patient", UUID.nameUUIDFromBytes(data.get(patientRefKey).getBytes())));
			
			// Do not forget to override the UUID to a unique one.
			pi.computeNewUUID(patientIdentifierTypeUuid, data);

			allEntities.add(pi);
		}
	}
}
