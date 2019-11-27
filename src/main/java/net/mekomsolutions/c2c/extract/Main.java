package net.mekomsolutions.c2c.extract;

import net.mekomsolutions.c2c.extract.route.Route1;

public class Main {

	public static void main(String args[]) throws Exception {

		org.apache.camel.main.Main main = new org.apache.camel.main.Main();
		main.addRouteBuilder(new Route1());
		main.setPropertyPlaceholderLocations("classpath:application.properties");
		main.run();
	}
}
