package main;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

public class LoggingTest {
	
	@Test
	public void TestLoggingInitialized() throws FileNotFoundException {
		Logger logger = LoggerFactory.getLogger(LoggingTest.class);
		logger.info("JUNIT-Test: Log4j-Initialization successful");
	}
}
