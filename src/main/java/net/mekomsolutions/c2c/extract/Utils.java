package net.mekomsolutions.c2c.extract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

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

}
