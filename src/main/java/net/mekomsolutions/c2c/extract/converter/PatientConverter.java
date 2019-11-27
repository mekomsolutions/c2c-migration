package net.mekomsolutions.c2c.extract.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import net.mekomsolutions.c2c.extract.Utils;
import net.mekomsolutions.c2c.extract.entity.Patient;
import net.mekomsolutions.c2c.extract.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonAttribute;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonName;

@Converter
public class PatientConverter {

	/**
	 * Transform the data input, passed as a parameter, into a Patient.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.extract.entity.Patient
	 */
	@Converter
	public Patient toPatient(HashMap<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<SyncEntity>();

		UUID patientUuid = UUID.nameUUIDFromBytes(data.get("objKey").getBytes());
		
		// Create the Patient
		{	
			SyncPatient patient = new SyncPatient(patientUuid.toString());
			patient.setAllergyStatus("Unknown");
			patient.setGender(data.get("gender"));
			patient.setBirthdate(Utils.convertBirthdate(data.get("dob")));

			patient.setPatientDateCreated(patient.getDateCreated());
			patient.setPatientCreatorUuid(patient.getCreatorUuid());
			allEntities.add(patient);
		}
		
		// Create the PersonName
		{
			// Build the UUID from a concatenation the firstName, lastName and dob
			String uuidBaseString = data.get("firstname") + data.get("lastname") + data.get("dob");
			SyncPersonName personName = new SyncPersonName(UUID.nameUUIDFromBytes(uuidBaseString.getBytes()).toString());
			personName.setPersonUuid(Utils.getModelClassLight("Patient", patientUuid));
			personName.setGivenName(data.get("firstname"));
			personName.setFamilyName(data.get("lastname"));
			allEntities.add(personName);
		}

		// Possibly create a Phone number Person Attribute
		if (! data.get("contactphone").isEmpty())
		{
			
			SyncPersonAttribute phoneNumberAttribute = new SyncPersonAttribute(data, exchange);

			String phoneNumberAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{pat.phoneNumber.uuid}}");
			phoneNumberAttribute.setPersonAttributeTypeUuid(
					Utils.getModelClassLight("PersonAttributeType",
					UUID.fromString(phoneNumberAttributeTypeUuid)));
			phoneNumberAttribute.setValue(data.get("contactphone"));

			phoneNumberAttribute.setPersonUuid(Utils.getModelClassLight("Patient", patientUuid));

			// Override the default UUID
			phoneNumberAttribute.setUuid(
					UUID.nameUUIDFromBytes((phoneNumberAttribute.getPersonAttributeTypeUuid() + 
					data.get("contactphone")).getBytes()).toString());

			allEntities.add(phoneNumberAttribute);
		}
		
		// TODO: Add Person Identifiers
		
		
		return new Patient(allEntities);
	}
}
