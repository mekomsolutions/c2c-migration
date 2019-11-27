package net.mekomsolutions.c2c.extract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.component.properties.PropertiesComponent;

import net.mekomsolutions.c2c.extract.entity.Contact;

public class Constants {
	
	// TOOD: This should be brought via the application.properties file.
	/**
	 * Refers to an existing user
	 */
	public static final String USER_UUID = "8295e796-4016-11e4-8e9b-2939a1809c8e";
	
	public static final Map<String, String> FULL_MODEL_CLASS_NAMES = createFull();
	
	public static final Map<String, String> LIGHT_MODEL_CLASS_NAMES = createLight();
    
    private static Map<String, String> createFull() {
        
    	Map<String, String> result = new HashMap<String, String>();
    	result.put("SyncPerson", "org.openmrs.sync.component.model.PersonModel");
    	result.put("SyncPersonName", "org.openmrs.sync.component.model.PersonNameModel");
    	result.put("SyncPatient", "org.openmrs.sync.component.model.PatientModel");
    	result.put("SyncPersonAttribute", "org.openmrs.sync.component.model.PersonAttributeModel");

        return Collections.unmodifiableMap(result);
    }
 
    private static Map<String, String> createLight() {
        
    	Map<String, String> result = new HashMap<String, String>();
        result.put("Patient", "org.openmrs.sync.component.entity.light.PatientLight");
        result.put("User", "org.openmrs.sync.component.entity.light.UserLight");
        result.put("PersonAttributeType", "org.openmrs.sync.component.entity.light.PersonAttributeTypeLight");

        return Collections.unmodifiableMap(result);
    }
 
}