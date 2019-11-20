package net.mekomsolutions.c2c.extract.converter;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;
import net.mekomsolutions.c2c.extract.Entity.Patient;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncPatient;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncPerson;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncPersonName;

@Converter
public class PatientConverter {

	@Converter
	public Patient toPatient(HashMap<String,String> data , Exchange exchange) {

		TypeConverter converter = exchange.getContext().getTypeConverter();
		List<Integer> lastModifiedDate = Utils.dateLongToArray(converter.convertTo(Long.class, data.get("lastModified")));

		UUID patientUuid = UUID.nameUUIDFromBytes(data.get("objKey").getBytes());
		SyncPatient patient = new SyncPatient(UUID.nameUUIDFromBytes(data.get("objKey").getBytes()).toString());
		{
			String defaultUser = Utils.getModelClassLight("User", UUID.fromString(Constants.DEFAULT_USER_UUID));

			// Created
			patient.setCreatorUuid(defaultUser);
			patient.setDateCreated(lastModifiedDate);
			
			// Changed
			patient.setChangedByUuid(defaultUser);
			patient.setDateChanged(lastModifiedDate);
			
			patient.setAllergyStatus("Unknown");
			patient.setGender(data.get("gender"));
			patient.setBirthdate(Utils.convertBirthdate(data.get("dob")));

			patient.setPatientDateCreated(lastModifiedDate);
			patient.setPatientCreatorUuid(defaultUser);
			
		}

		// Build the UUID from a concatenation the firstName, lastName and dob
		String uuidBaseString = data.get("firstname") + data.get("lastname") + data.get("dob");
		SyncPersonName personName = new SyncPersonName(UUID.nameUUIDFromBytes(uuidBaseString.getBytes()).toString());
		{
			String defaultUser = Utils.getModelClassLight("User", UUID.fromString(Constants.DEFAULT_USER_UUID));
			personName.setCreatorUuid(defaultUser);
			personName.setDateCreated(lastModifiedDate);
			personName.setPersonUuid(Utils.getModelClassLight("Patient", patientUuid));
			personName.setGivenName(data.get("firstname"));
			personName.setFamilyName(data.get("lastname"));
		}

		return new Patient(patient, personName);
	}
}
