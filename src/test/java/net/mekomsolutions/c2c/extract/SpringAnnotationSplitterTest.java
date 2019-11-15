package net.mekomsolutions.c2c.extract;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAnnotationSplitterTest extends CamelSpringTestSupport {
    
	@Override
	protected AnnotationConfigApplicationContext createApplicationContext() {
    	return new AnnotationConfigApplicationContext();
	}
	
	@Test
    public void testSplitJsonpath() throws Exception {
		
        MockEndpoint mockSplit = getMockEndpoint("mock:split");
        mockSplit.setAssertPeriod(500);
        mockSplit.expectedMessageCount(1000);
        assertMockEndpointsSatisfied();
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
    	
		PropertiesComponent prop = context.getComponent(
				"properties", PropertiesComponent.class);
		prop.setLocation("classpath:application.properties");		

    	return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data/inbox/?noop=true")
                .split().jsonpath("$.[*].{{couchdb.bucket.name}}").streaming()
                .log("Split line ${body}")
                        .to("seda:split")
                        .to("mock:split");
            }
        };
    }
}
