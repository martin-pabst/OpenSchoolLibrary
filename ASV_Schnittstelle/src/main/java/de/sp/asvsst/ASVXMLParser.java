package de.sp.asvsst;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sp.asvsst.model.ASVExport;
import de.sp.tools.server.progressServlet.ProgressServlet;
import de.sp.tools.xml.DateFormatTransformer;

public class ASVXMLParser {

	public ASVExport parseASVXMLData(String filename, String password,
			String progressCode) {

		ProgressServlet.publishProgress(0, 120, 2, "Entpacke und lese ASV-Daten", false, "", progressCode);
		
		try {

			ZipFile zipFile = new ZipFile(filename);

			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password);
			}

			FileHeader fileHeader = zipFile.getFileHeader("export.xml");

			DateFormat format = new SimpleDateFormat("dd.MM.YYYY",
					Locale.GERMANY);

			RegistryMatcher m = new RegistryMatcher();
			m.bind(Date.class, new DateFormatTransformer(format));

			Serializer serializer = new Persister(m);

			try (InputStream is = zipFile.getInputStream(fileHeader)) {

				ASVExport asvExport = serializer.read(ASVExport.class, is,
						false);

				return asvExport;
				
			}


		} catch (Exception e) {
			
			Logger logger = LoggerFactory.getLogger(ASVXMLParser.class);
			logger.error("Fehler beim Import der ASV-Daten: ", e);
			
			String result = e.toString();
			
			if(e.toString().contains("Wrong Password")){
				result = "Falsches Passwort für die Zip-Datei!";
			}
			
			ProgressServlet.publishProgress(0, 100, 100, "Fehler beim Lesen der ASV-Daten", true, result, progressCode);
			
		}

		return null;
		
	}


}
