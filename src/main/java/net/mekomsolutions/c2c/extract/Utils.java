package net.mekomsolutions.c2c.extract;

import java.util.UUID;

public class Utils {

	public static <T> String getModelClassNameFromType(Class<T> type) {
		return getModelClassNameFromString(type.getSimpleName());
	}
	
	public static String getModelClassNameFromString(String typeAsString) {
		return Constants.MODEL_CLASS_NAMES.get(typeAsString);
	}
	
	public static String getModelClassWithRef(String typeAsString, UUID uuid) {
		return getModelClassNameFromString(typeAsString) + "(" + uuid.toString() + ")";
	}
}
