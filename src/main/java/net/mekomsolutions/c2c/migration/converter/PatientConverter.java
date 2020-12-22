package net.mekomsolutions.c2c.migration.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.Address;
import net.mekomsolutions.c2c.migration.entity.Patient;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonAddress;
import net.mekomsolutions.c2c.migration.entity.sync.SyncPersonName;

@Converter
public class PatientConverter {

	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String MIDDLE_NAME = "middlename";
	private static final String DOB = "dob";
	private static final String GENDER = "gender";
	private static final String PHONE = "contactphone";

	private static final String MARITAL_STATUS = "maritalstatus";
	private static final String EMPLOYMENT = "employment";

	private static final String DOSSIER_NUMBER = "dossiernumber";
	private static final String VECNA_ID = "vecnaid";
	private static final String VECNA_GUID = "vecnaguid";

	private static final String ADDRESS_LINE_1 = "addressline1";
	private static final String ADDRESS_LINE_2 = "addressline2";

	/**
	 * Transform the data input, passed as a parameter, into a Patient.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.migration.entity.Patient
	 */
	@Converter
	public Patient toPatient(Map<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<>();

		UUID patientUuid = UUID.nameUUIDFromBytes(data.get(Constants.OBJECT_KEY).getBytes());

		// Patient
		{	
			SyncPatient patient = new SyncPatient(data, exchange);
			patient.setUuid(patientUuid.toString());
			patient.setAllergyStatus("Unknown");
			patient.setGender(exchange.getContext().resolvePropertyPlaceholders("{{gender." + 
					data.get(GENDER).toString().toLowerCase() + "}}"));
			patient.setBirthdate(Utils.convertBirthdate(data.get(DOB)));
			patient.setPatientDateCreated(patient.getDateCreated());
			patient.setPatientCreatorUuid(patient.getCreatorUuid());

			allEntities.add(patient);
		}

		// Person Name
		{
			// Build the UUID from a concatenation the firstName, middleName, lastName and dob
			SyncPersonName personName = new SyncPersonName(data, exchange);
			String uuidBaseString = FIRST_NAME + MIDDLE_NAME + LAST_NAME + DOB + data.get(Constants.OBJECT_KEY);
			personName.setUuid(UUID.nameUUIDFromBytes(uuidBaseString.getBytes()).toString());
			personName.setPerson(Utils.getModelClassLight("Patient", patientUuid));
			personName.setGivenName(data.get(FIRST_NAME));
			personName.setMiddleName(data.get(MIDDLE_NAME));
			personName.setFamilyName(data.get(LAST_NAME));
			personName.setPreferred(true);

			allEntities.add(personName);
		}

		// Person Attribute: Phone Number
		SyncEntityUtils.createAndAddPersonAttribute("pat.phoneNumber.uuid", data.get(PHONE), Constants.OBJECT_KEY, data, exchange, allEntities);
		// Person Attribute: Marital Status
		SyncEntityUtils.createAndAddPersonAttribute("pat.maritalStatus.uuid", data.get(MARITAL_STATUS), Constants.OBJECT_KEY, data, exchange, allEntities);
		// Person Attribute: Employment
		SyncEntityUtils.createAndAddPersonAttribute("pat.employment.uuid", data.get(EMPLOYMENT), Constants.OBJECT_KEY, data, exchange, allEntities);

		// Dossier Number (set as the Dossier Number plus a "0" in front to make it 8 chars long)
		SyncEntityUtils.createAndAddPatientIdentifier("pit.numeroDossier.uuid", "0" +  data.get(DOSSIER_NUMBER), Constants.OBJECT_KEY, true, data, exchange, allEntities);
		// Ancien Dossier Number
		SyncEntityUtils.createAndAddPatientIdentifier("pit.ancienNumeroDossier.uuid", data.get(DOSSIER_NUMBER), Constants.OBJECT_KEY, true, data, exchange, allEntities);
		// Vecna ID
		SyncEntityUtils.createAndAddPatientIdentifier("pit.vecnaId.uuid", data.get(VECNA_ID), Constants.OBJECT_KEY, false, data, exchange, allEntities);
		// Vecna GUID
		SyncEntityUtils.createAndAddPatientIdentifier("pit.vecnaGuid.uuid", data.get(VECNA_GUID), Constants.OBJECT_KEY, false, data, exchange, allEntities);

		// Address
		{
			SyncPersonAddress personAddress = new SyncPersonAddress(data, exchange);

			Address address = new Address();
			address.setAddress1(data.get(ADDRESS_LINE_1));
			address.setAddress2(data.get(ADDRESS_LINE_2));

			personAddress.setAddress(address);
			personAddress.setPerson(Utils.getModelClassLight("Patient", patientUuid));
			personAddress.setUuid(SyncEntityUtils.computeNewUUID(personAddress.getAddress().getAddress1() + personAddress.getAddress().getAddress2(), data.get(Constants.OBJECT_KEY), data));
			allEntities.add(personAddress);
		}

		return new Patient(allEntities);
	}
}
