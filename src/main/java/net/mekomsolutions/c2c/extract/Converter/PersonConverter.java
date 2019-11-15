package net.mekomsolutions.c2c.extract.Converter;

import java.util.HashMap;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.extract.Entity.Person;

@Converter
public class PersonConverter {

	@Converter
	public static Person toPerson(HashMap<String,String> data , Exchange exchange) {

		UUID uuid = UUID.nameUUIDFromBytes(data.get("objKey").getBytes());
		
		// While we're at it, set new headers
		exchange.getIn().setHeader("type", Person.class.getSimpleName());
		exchange.getIn().setHeader("uuid", uuid.toString());
		
		return new Person(uuid.toString(),
				data.get("firstname"),
				data.get("middlename"),
				data.get("lastname"));
	}
}
