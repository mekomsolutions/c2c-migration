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

	public static <T> String getModelClassNameFromType(Class<T> type) {
		return getModelClassNameFromString(type.getSimpleName());
	}
	
	public static String getModelClassNameFromString(String typeAsString) {
		return Constants.MODEL_CLASS_NAMES.get(typeAsString);
	}
	
	public static String getModelClassWithRef(String typeAsString, UUID uuid) {
		return getModelClassNameFromString(typeAsString) + "(" + uuid.toString() + ")";
	}
	
	public static List<Integer> dateLongToArray(Long timeInMillisecs) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timeInMillisecs);
		
		List<Integer> array = new ArrayList<Integer>();
		array.add(calendar.get(Calendar.YEAR));
		array.add(calendar.get(Calendar.MONTH));
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
		array.add(calendar.get(Calendar.MONTH));
		array.add(calendar.get(Calendar.DAY_OF_MONTH));
		array.add(calendar.get(Calendar.HOUR));
		array.add(calendar.get(Calendar.MINUTE));
		array.add(calendar.get(Calendar.SECOND));
		return array;
	}

}
