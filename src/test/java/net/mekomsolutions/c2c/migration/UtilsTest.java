package net.mekomsolutions.c2c.migration;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import net.mekomsolutions.c2c.migration.Utils;

public class UtilsTest {

	@Test
	public void shouldReturnBirthdateAsArray() {
		
		String dateString = "1995-12-24T00:00:00Z";
		List<Integer> dateArray = Utils.convertBirthdate(dateString);
		
		List<Integer> expectedDate = Arrays.asList(
				Integer.valueOf("1995"),
				Integer.valueOf("12"),
				Integer.valueOf("24"));
		assertEquals(expectedDate, dateArray);
		}
	
	@Test
	public void shouldReturnDateAsArray() {
		
		Long timeInMilllisecs = 1556901962881L;
		List<Integer> dateArray = Utils.dateLongToArray(timeInMilllisecs);
		
		assertEquals(6, dateArray.size());
		List<Integer> expectedDate = Arrays.asList(
				Integer.valueOf("2019"),
				Integer.valueOf("5"),
				Integer.valueOf("3"),
				Integer.valueOf("6"),
				Integer.valueOf("46"),
				Integer.valueOf("2"));
		assertEquals(expectedDate, dateArray);
		}
	
	@Test
	public void shouldReturnConcatenatedName() {
		LinkedList<String> list =  new LinkedList<String>(Arrays.asList("firstname  ", "" , " lastname", null));
		String name = Utils.concatName(list);
		
		assertEquals("Firstname Lastname", name);
		
		// Verify that it handles 'tab' character
		list =  new LinkedList<String>(Arrays.asList("	", "" , " lastname", null));
		name = Utils.concatName(list);
		assertEquals("Lastname", name);
		
	}
	
	@Test
	public void shouldReturnConcatenatedPhoneNumber() {
		LinkedList<String> list =  new LinkedList<String>(Arrays.asList(" 0234", "" , " 98765", null));
		String name = Utils.concatPhoneNumber(list);
		
		assertEquals("0234 / 98765", name);
	}
}
