package main.config;

import org.junit.Test;

import de.sp.main.config.Configuration;

public class ConfigurationTest {
	
	@Test
	public void testReadConfiguration() throws Exception {
		
		Configuration c = Configuration.getInstance();
		
		org.junit.Assert.assertNotNull(c.getDatabase().getType());
		
	}
}
