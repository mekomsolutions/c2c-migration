package net.mekomsolutions.c2c.extract.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;
import net.mekomsolutions.c2c.extract.entity.Patient;
import net.mekomsolutions.c2c.extract.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.extract.entity.sync.SyncEntityUtils;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonAttribute;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonName;

@Converter
public class PatientConverter {

	private static String FIRST_NAME = "firstname";
	private static String LAST_NAME = "lastname";
	private static String MIDDLE_NAME = "middlename";
	private static String DOB = "dob";
	private static String GENDER = "gender";
	private static String PHONE = "contactphone";
	private static String MARITAL_STATUS = "maritalstatus";
	private static String EMPLOYMENT = "employment";

	/**
	 * Transform the data input, passed as a parameter, into a Patient.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.extract.entity.Patient
	 */
	@Converter
	public Patient toPatient(HashMap<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<SyncEntity>();

		UUID patientUuid = UUID.nameUUIDFromBytes(data.get(Constants.OBJECT_KEY).getBytes());

		// Patient
		{	
			SyncPatient patient = new SyncPatient(data, exchange);
			patient.setUuid(patientUuid.toString());
			patient.setAllergyStatus("Unknown");
			patient.setGender(data.get(GENDER));
			patient.setBirthdate(Utils.convertBirthdate(data.get(DOB)));

			patient.setPatientDateCreated(patient.getDateCreated());
			patient.setPatientCreatorUuid(patient.getCreatorUuid());

			allEntities.add(patient);
		}

		// Person Name
		{
			// Build the UUID from a concatenation the firstName, middleName, lastName and dob
			SyncPersonName personName = new SyncPersonName(data, exchange);
			String uuidBaseString = data.get(FIRST_NAME) + data.get(MIDDLE_NAME) + data.get(LAST_NAME) + data.get(DOB);
			personName.setUuid(UUID.nameUUIDFromBytes(uuidBaseString.getBytes()).toString());
			//			SyncPersonName personName = new SyncPersonName(UUID.nameUUIDFromBytes(uuidBaseString.getBytes()).toString());
			personName.setPersonUuid(Utils.getModelClassLight("Patient", patientUuid));
			personName.setGivenName(data.get(FIRST_NAME));
			personName.setMiddleName(data.get(MIDDLE_NAME));
			personName.setFamilyName(data.get(LAST_NAME));

			allEntities.add(personName);
		}

		// Person Attribute: Phone Number
		SyncEntityUtils.createAndAddPersonAttribute("pat.contactPersonPhone.uuid", data.get(PHONE), Constants.OBJECT_KEY, data, exchange, allEntities);
		// Person Attribute: Marital Status
		SyncEntityUtils.createAndAddPersonAttribute("pat.maritalStatus.uuid", data.get(MARITAL_STATUS), Constants.OBJECT_KEY, data, exchange, allEntities);
		// Person Attribute: Employment
		SyncEntityUtils.createAndAddPersonAttribute("pat.employment.uuid", data.get(EMPLOYMENT), Constants.OBJECT_KEY, data, exchange, allEntities);

		// Dossier Number
		
		
		return new Patient(allEntities);
	}
}
