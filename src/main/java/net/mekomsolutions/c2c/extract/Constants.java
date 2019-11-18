package net.mekomsolutions.c2c.extract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mekomsolutions.c2c.extract.Entity.Person;

public class Constants {
	
    public static final Map<String, String> MODEL_CLASS_NAMES = createMap();

    private static Map<String, String> createMap() {
        
    	Map<String, String> result = new HashMap<String, String>();
        result.put(Person.class.getSimpleName(), "org.openmrs.sync.component.model.PersonModel");
        result.put("PatientLight", "org.openmrs.sync.component.model.PatientLight");

        return Collections.unmodifiableMap(result);
    }
}