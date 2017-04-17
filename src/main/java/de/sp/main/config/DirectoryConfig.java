package de.sp.main.config;

import org.simpleframework.xml.Element;

public class DirectoryConfig {
	
	@Element(required = false)
	private String loggingconfigfilename = "configuration/log4j.xml";
	
	@Element(required = false)
	private String webcontentdirectory = "WebContent";

	public String getLoggingconfigfilename() {
		return loggingconfigfilename;
	}

	public String getWebcontentdirectory() {
		return webcontentdirectory;
	}

	
	
	
	
}
