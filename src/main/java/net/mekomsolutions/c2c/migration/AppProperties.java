package net.mekomsolutions.c2c.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

	private static Properties properties;

	public static String getProperty(String propertyName) {
		if (properties == null) {
			properties = setProperties();
		}
		return properties.getProperty(propertyName);
	}

	private static Properties setProperties() {
		Properties props = new Properties();
		try (InputStream input = AppProperties.class.getClassLoader()
				.getResourceAsStream("application.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find application.properties");
			}
			props.load(input);
			return props;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return props;
	}
}