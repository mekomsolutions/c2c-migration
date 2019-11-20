package net.mekomsolutions.c2c.extract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mekomsolutions.c2c.extract.Entity.Contact;

public class Constants {
	
	public static final Map<String, String> FULL_MODEL_CLASS_NAMES = createFull();
	
	public static final Map<String, String> LIGHT_MODEL_CLASS_NAMES = createLight();
    
    private static Map<String, String> createFull() {
        
    	Map<String, String> result = new HashMap<String, String>();
    	result.put("SyncPerson", "org.openmrs.sync.component.model.PersonModel");
    	result.put("SyncPersonName", "org.openmrs.sync.component.model.PersonNameModel");
    	result.put("SyncPatient", "org.openmrs.sync.component.model.PatientModel");

        return Collections.unmodifiableMap(result);
    }
 
    private static Map<String, String> createLight() {
        
    	Map<String, String> result = new HashMap<String, String>();
        result.put("Patient", "org.openmrs.sync.component.entity.light.PatientLight");
        result.put("User", "org.openmrs.sync.component.entity.light.UserLight");

        return Collections.unmodifiableMap(result);
    }
 
    public static final String DEFAULT_USER_UUID = "29abb186-0b0b-11ea-8c69-6fb8f76f2a04";
}