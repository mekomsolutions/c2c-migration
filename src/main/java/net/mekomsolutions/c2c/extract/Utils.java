package net.mekomsolutions.c2c.extract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public class Utils {

	public static <T> String getModelClassFullFromType(Class<T> type) {
		return getModelClassFullFromString(type.getSimpleName());
	}
	
	public static String getModelClassFullFromString(String typeAsString) {
		return Constants.FULL_MODEL_CLASS_NAMES.get(typeAsString);
	}
		
	public static String getModelClassLight(String typeAsString, UUID uuid) {
		return Constants.LIGHT_MODEL_CLASS_NAMES.get(typeAsString) + "(" + uuid.toString() + ")";
	}
	
	public static List<Integer> dateLongToArray(Long timeInMillisecs) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timeInMillisecs);
		
		List<Integer> array = new ArrayList<Integer>();
		array.add(calendar.get(Calendar.YEAR));
		array.add(calendar.get(Calendar.MONTH) + 1);
		array.add(calendar.get(Calendar.DAY_OF_MONTH));
		array.add(calendar.get(Calendar.HOUR));
		array.add(calendar.get(Calendar.MINUTE));
		array.add(calendar.get(Calendar.SECOND));
		return array;
	}

	public static List<Integer> dateStringToArray(String formattedDateString) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		Date date = new Date();
		try {
			date  = format.parse(formattedDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		List<Integer> array = new ArrayList<Integer>();
		array.add(calendar.get(Calendar.YEAR));
		array.add(calendar.get(Calendar.MONTH) + 1);
		array.add(calendar.get(Calendar.DAY_OF_MONTH));
		array.add(calendar.get(Calendar.HOUR));
		array.add(calendar.get(Calendar.MINUTE));
		array.add(calendar.get(Calendar.SECOND));
		return array;
	}

	public static List<Integer> convertBirthdate(String string) {
		List<Integer> birthdate = dateStringToArray(string);
		return birthdate.subList(0, 3);
	}
	
	private static List<String> trimAndCapitalize(LinkedList<String> list) {

		list.removeAll(Arrays.asList("", null));
		
		// Trim
		List<String> trimmedList = new ArrayList<String>();
		for (String str : list) {
			trimmedList.add(str.trim());
		}
		// Capitalize
		List<String> capitalizedList = new ArrayList<String>();
		for (String str : trimmedList) {
			capitalizedList.add(str.substring(0, 1).toUpperCase() + str.substring(1));
		}

		return capitalizedList;
	}
	
	public static String concatName (LinkedList<String> list) {
		String name = String.join(" ", trimAndCapitalize(list));
		return name;
	}

	public static String concatPhoneNumber (LinkedList<String> list) {
		String name = String.join(" / ", trimAndCapitalize(list));
		return name;
	}

}
