package net.mekomsolutions.c2c.extract.converter;

import java.util.HashMap;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;
import net.mekomsolutions.c2c.extract.Entity.Person;

@Converter
public class PersonConverter {

	@Converter
	public Person toPerson(HashMap<String,String> data , Exchange exchange) {

		UUID personUuid = UUID.nameUUIDFromBytes(data.get("objKey").getBytes());
		String patientUuid = Utils.getModelClassWithRef("PatientLight", UUID.nameUUIDFromBytes(data.get("patientKey").getBytes()));
		String modelClassName = Utils.getModelClassNameFromType(Person.class);
		
		return new Person(modelClassName,
				personUuid.toString(),
				patientUuid,
				data.get("firstname"),
				data.get("middlename"),
				data.get("lastname"));
	}
}
