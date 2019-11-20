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
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPatient;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPerson;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPersonName;

@Converter
public class PatientConverter {

	@Converter
	public Patient toPatient(HashMap<String,String> data , Exchange exchange) {


		OpenMRSPatient patient = new OpenMRSPatient(UUID.nameUUIDFromBytes(data.get("objKey").getBytes()).toString());
		{
			String defaultUser = Utils.getModelClassWithRef("UserLight", UUID.fromString(Constants.DEFAULT_USER_UUID));

			patient.setCreatorUuid(defaultUser);
			patient.setDateCreated(null);
			patient.setChangedByUuid(defaultUser);

			TypeConverter converter = exchange.getContext().getTypeConverter();
			List<Integer> dateAsList = Utils.dateLongToArray(converter.convertTo(Long.class, data.get("lastModified")));
			patient.setDateChanged(dateAsList);
			patient.setGender(data.get("gender"));
			patient.setBirthdate(Utils.dateStringToArray(data.get("dob")));

			patient.setPatientCreatorUuid(defaultUser);
			patient.setChangedByUuid(defaultUser);
		}

		// Patient UUID and Person UUID are the same. 
		OpenMRSPerson person = new OpenMRSPerson(UUID.nameUUIDFromBytes(data.get("objKey").getBytes()).toString());
		{
			String defaultUser = Utils.getModelClassWithRef("UserLight", UUID.fromString(Constants.DEFAULT_USER_UUID));
			person.setCreatorUuid(defaultUser);
			// TODO: Implement the converter for Person
		}

		// TODO: Identify how we should set the UUID to the PersonName.
		OpenMRSPersonName personName = new OpenMRSPersonName(UUID.nameUUIDFromBytes(data.get("objKey").getBytes()).toString());
		{
			String defaultUser = Utils.getModelClassWithRef("UserLight", UUID.fromString(Constants.DEFAULT_USER_UUID));
			personName.setCreatorUuid(defaultUser);
			// TODO: Implement the converter for PersonName
		}

		return new Patient(patient, person, personName);
	}
}
