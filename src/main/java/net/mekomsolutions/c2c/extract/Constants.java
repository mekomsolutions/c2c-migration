package net.mekomsolutions.c2c.extract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mekomsolutions.c2c.extract.Entity.Contact;

public class Constants {
	
    public static final Map<String, String> MODEL_CLASS_NAMES = createMap();

    private static Map<String, String> createMap() {
        
    	Map<String, String> result = new HashMap<String, String>();
    	result.put("OpenMRSPerson", "org.openmrs.sync.component.model.PersonModel");
    	result.put("OpenMRSPersonName", "org.openmrs.sync.component.model.PersonNameModel");
    	result.put("OpenMRSPatient", "org.openmrs.sync.component.model.PatientModel");
        result.put("PatientLight", "org.openmrs.sync.component.model.PatientLight");
        result.put("UserLight", "org.openmrs.sync.component.model.UserLight");

        return Collections.unmodifiableMap(result);
    }
    
    public static final String DEFAULT_USER_UUID = "29abb186-0b0b-11ea-8c69-6fb8f76f2a04";
}