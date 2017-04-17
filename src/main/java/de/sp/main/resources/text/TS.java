package de.sp.main.resources.text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sp.main.config.Configuration;
import de.sp.main.config.LanguageCode;
import de.sp.main.config.LanguageConfig;
import de.sp.main.config.LanguagesConfig;
import de.sp.tools.file.FileTool;

public class TS {

	private static ArrayList<LanguageConfig> languages = new ArrayList<>(); // all
																			// loaded
	private static HashMap<String, LanguageConfig> codeToLanguageConfigMap = new HashMap<String, LanguageConfig>();

	private static ArrayList<LanguageConfig> fallbackLanguages = new ArrayList<>();

	private LanguageConfig currentLanguage = null;

	private TextMap currentMap = new TextMap(); // Empty Map to avoid
												// Exceptions when used
												// before initialization

	private static Logger logger;

	public TS(String languageCode) {

		Configuration config;

		if (languageCode == null) {

			config = Configuration.getInstance();

			LanguagesConfig languagesConfig = config.getLanguagesConfig();

			languageCode = languagesConfig.defaultCode;

		}

		selectLanguage(codeToLanguageConfigMap.get(languageCode));

	}

	public String get(String path) {

		if(path == null || path.isEmpty()){
			return "";
		}
		
		String firstGuess = currentMap.get(path);

		if (firstGuess != null) {
			return firstGuess;
		}

		logger.debug("Text for path " + path + " not found in language "
				+ currentLanguage.englishName);

		for (LanguageConfig lc : fallbackLanguages) {

			String nextGuess = lc.textMap.get(path);

			if (nextGuess != null) {

				return nextGuess;

			}
		}

		logger.info("Text for path " + path + " not found in language "
				+ currentLanguage.englishName
				+ " including fallback languages.");

		return path;

	}

	public static void readTextResources() throws Exception {

		logger = LoggerFactory.getLogger(TS.class);

		Configuration config = Configuration.getInstance();

		LanguagesConfig languagesConfig = config.getLanguagesConfig();

		for (LanguageConfig lc : languagesConfig.languages) {

			languages.add(lc);

			codeToLanguageConfigMap.put(lc.languageCode, lc);

		}

		
		
//		ArrayList<String> filenames = FileTool.listFiles("/text");
		List<String> filenames = FileTool.listAllResourceFiles("/text");

		for (String filename : filenames) {

			Serializer serializer = new Persister();

			// This works inside jarfile ...
			InputStream in = TS.class.getResourceAsStream(filename);

			if (in == null) {

				// ... this works outside jarfile
				in = TS.class.getClassLoader().getResourceAsStream(
						filename);

			}
			TextResourceFile textResourceFile = serializer.read(
					TextResourceFile.class, in);

//			File file = new File(filename);
//
//			TextResourceFile textResourceFile = serializer.read(
//					TextResourceFile.class, file);

			for (TextResources textResources : textResourceFile.textResources) {

				LanguageConfig lc = codeToLanguageConfigMap
						.get(textResources.language);

				if (lc != null) {

					TextMap textMap = lc.textMap;

					String globalPath = textResources.path;

					if (!globalPath.endsWith(".")) {
						globalPath += ".";
					}

					for (TextResource tr : textResources.resources) {
						textMap.put(globalPath + tr.path, tr.text);
					}

				} else {
					logger.warn("Error reading language file " + filename
							+ ": Language " + textResources.language
							+ " is not registered in configuration file.");
				}

			}
			
		}

	}

	private void selectLanguage(LanguageConfig languageConfig) {

		currentLanguage = languageConfig;

		currentMap = languageConfig.textMap;

		for (LanguageCode fallback : languageConfig.fallbackCodes) {

			LanguageConfig fallbackConfig = codeToLanguageConfigMap
					.get(fallback.languageCode);

			if (fallbackConfig != null) {

				fallbackLanguages.add(codeToLanguageConfigMap
						.get(fallback.languageCode));

			}
		}

	}



}
