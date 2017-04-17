package de.sp.main.config;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sp.main.login.LoginServlet;

@XmlRootElement
public class Configuration {

	@Element
	private DatabaseConfig database;

	@Element
	private DirectoryConfig directories;

	@Element(name = "languages")
	private LanguagesConfig languagesConfig;
	
	@Transient
	private static Configuration instance;

	@Transient
	private static Logger logger;

	public static Configuration getInstance() {

		if (instance == null) {
			try {

				logger = LoggerFactory.getLogger(LoginServlet.class);
				Serializer serializer = new Persister();
				File file = new File("configuration/configuration.xml");

				instance = serializer.read(Configuration.class, file);

				return instance;

			} catch (Exception e) {
				logger.error(
						"Severe Error: Configuration has not been loaded.", e);
				return new Configuration(); // only in case exception had been thrown
			}
		}

		return instance;
	}

	public DatabaseConfig getDatabase() {
		return database;
	}

	public DirectoryConfig getDirectories() {
		return directories;
	}

	public LanguagesConfig getLanguagesConfig() {

		return languagesConfig;

	}


	
}
