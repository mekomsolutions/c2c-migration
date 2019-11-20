package net.mekomsolutions.c2c.extract.converter;

import java.util.HashMap;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.extract.Entity.Contact;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPerson;

@Converter
public class ContactConverter {

	@Converter
	public Contact toPerson(HashMap<String,String> data , Exchange exchange) {

		OpenMRSPerson person  = new OpenMRSPerson(UUID.nameUUIDFromBytes(data.get("objKey").getBytes()).toString());
		{
//			person.setPatientChangedByUuid(Utils.getModelClassWithRef("PatientLight", UUID.nameUUIDFromBytes(data.get("patientKey").getBytes()));
				
		}
		
		return new Contact(person);
	}
}
