package de.sp.asvsst.databasewriter;

import de.sp.asvsst.database.daos.ASVSStDAO;
import de.sp.asvsst.model.*;
import de.sp.asvsst.model.asvwlstore.ASVWlStore;
import de.sp.asvsst.model.wertelisten.ASVBesuchterReligionsunterricht;
import de.sp.asvsst.model.wertelisten.ASVJahrgangsstufe;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.DBClassDAO;
import de.sp.database.daos.basic.SchoolTermDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.model.*;
import de.sp.database.stores.SchoolTermStore;
import de.sp.database.stores.ValueListStore;
import de.sp.database.valuelists.ValueListType;
import de.sp.main.StartServer;
import de.sp.tools.server.progressServlet.ProgressServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ASVToDatabaseWriter {

	private StringBuilder protocol;
	private String progressCode;
	private Logger logger;

	private int schuelerZaehler;
	private int schuelerAnzahl;

	private HashMap<String, Value> externalJahrgangsstufeKeyToValueMap;

	public ASVToDatabaseWriter(String progressCode) {
		super();
		this.progressCode = progressCode;
		logger = LoggerFactory.getLogger(StartServer.class);
	}

	public void writeToDatabase(ASVExport asvExport) {


		protocol = new StringBuilder();

		publishProgress(0, 120, 20,
				"Schreibe die ASV-Daten in die Datenbank...", false, "",
				progressCode);

		List<School> dbSchools = SchoolTermStore.getInstance().getSchools();

		String result = "";

		try (Connection con = ConnectionPool.beginTransaction()) {

			try {

				for (ASVSchule asvSchule : asvExport.schulen) {

					for (School school : dbSchools) {

						if (asvSchule.schulnummer.equals(school.getNumber())) {

							protocolAdd("Importiere die ASV-Daten von Schule "
									+ asvSchule.dienststellenname + "("
									+ asvSchule.schulnummer + ")\n\n", true,
									"#0000ff", 0);

							ASVBesuchterReligionsunterricht.init(con, school.getId());

							initMaps(school.getId());

							importSchool(asvSchule, school, asvExport, con);

						}

					}
				}

				con.commit();

				result = "<h1>Import beendet</h1><h2>Protokoll:</h2>";

			} catch (Exception e) {

				con.rollback();

				result = "<h1 style=\"color: red\">Import mit Fehler beendet</h1>";

				logger.error("Fataler Fehler beim Import der ASV-Daten: ", e);
				protocolAdd(
						"Fataler Fehler beim Import der ASV-Daten:"
								+ e.toString() + "\n\n", true, "#ff0000", 0);
			}

		}

		ASVWlStore.releaseMemory();

		publishProgress(0, 120, 20, "Fertig!", true, result
				+ protocol.toString(), progressCode);

	}

	private void initMaps(Long school_id) {

		// Initialize externalJahrgangsstufeKeyToValueMap

		externalJahrgangsstufeKeyToValueMap = new HashMap<>();

		for (Value value : ValueListStore.getInstance().getValueList(school_id, ValueListType.form.getKey())) {
			externalJahrgangsstufeKeyToValueMap.put(value.getExternal_key(), value);
		}


	}

	private void publishProgress(int min, int max, int now,
								 String text, boolean completed, Object result, String progressCode){


		if (progressCode != null) {
			ProgressServlet.publishProgress(min, max, now, text, completed, result, progressCode);
		} else {

			int percent = (int)((double)now/(double)max * 100);
		    System.out.println("" + percent + "%: " + text);
        }

	}

	private void importSchool(ASVSchule asvSchule, School school,
			ASVExport asvExport, Connection con) throws Exception {

		publishProgress(0, 120, 20,
				"Schreibe die ASV-Daten von Schule "
						+ asvSchule.dienststellenname + " in die Datenbank...",
				false, "", progressCode);

		schuelerAnzahl = asvSchule.getSchuelerAnzahl();

		// Division durch 0 später vermeiden
		if (schuelerAnzahl == 0) {
			schuelerAnzahl = 1;
		}

		schuelerZaehler = 0;

		SchoolTerm schoolTerm = getOrCreateSchoolTerm(asvSchule, school, con);

		// Wir nehmen alle Schüler/innen aus den Klassen und ordnen sie dann
		// anhand des ASV-Imports neu zu.
		ASVSStDAO.deleteStudentClassReferendes(schoolTerm.getId(), con);

		List<DBClass> classes = DBClassDAO.getAll(con);
		HashMap<String, DBClass> classMap = new HashMap<>();
		classes.forEach(dbClass -> classMap.put(dbClass.getName(), dbClass));

		for (ASVKlasse klasse : asvSchule.klassen) {
			importKlasse(klasse, asvSchule, school, classMap, schoolTerm, con);
		}

		LehrkraefteWriter lw = new LehrkraefteWriter(this, asvSchule, school, schoolTerm, asvExport, con, progressCode);
		lw.start();
		
	}

	private SchoolTerm getOrCreateSchoolTerm(ASVSchule asvSchule,
			School school, Connection con) throws ParseException, Exception {

		SchoolTerm schoolTerm = SchoolTermStore.getInstance().getTerm(school.getId(), asvSchule.schuljahr);

		if (schoolTerm == null) {

			String jahr1String = asvSchule.schuljahr.substring(0, 4);
			int jahr1 = Integer.parseInt(jahr1String);
			int jahr2 = jahr1 + 1;

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date begin;

			begin = sdf.parse("01.08." + jahr1);
			Date end = sdf.parse("31.07." + jahr2);
			protocolAdd("Lege Schuljahr " + asvSchule.schuljahr
					+ " in der Datenbank an.\n", false, "#0000ff", 1);
			schoolTerm = SchoolTermDAO.insert(school, asvSchule.schuljahr, begin, end, con);

			SchoolTermStore.getInstance().addSchoolTerm(schoolTerm);

		}

		return schoolTerm;
	}

	private void importKlasse(ASVKlasse klasse, ASVSchule asvSchule,
			School school, HashMap<String, DBClass> classMap,
			SchoolTerm schoolTerm, Connection con) throws Exception {

		DBClass dbClass = classMap.get(klasse.klassenname);

		if (dbClass == null) {

			protocolAdd("Lege Klasse " + klasse.klassenname + " im Schuljahr "
					+ schoolTerm.getName() + " an.", true, "#000000",
					2);

			String jahrgangsstufeSchluessel = "051";

			if (klasse.klassengruppen.size() > 0) {
				ASVKlassengruppe kg = klasse.klassengruppen.get(0);
				jahrgangsstufeSchluessel = kg.jahrgangsstufeSchluessel;
			}

			// Wertelisteneintrag für die Jahrgangsstufe holen
/*
			Value jahrgangsstufeValue = ValueDAO
					.findBySchoolAndValueStoreAndExternalKey(school.getId(),
							ValueListType.form.getKey(), jahrgangsstufeSchluessel,
							con);
*/

			Value jahrgangsstufeValue = externalJahrgangsstufeKeyToValueMap.get(jahrgangsstufeSchluessel);

			if (jahrgangsstufeValue == null) {

				ASVJahrgangsstufe asvjgst = ASVJahrgangsstufe
						.findBySchluessel(jahrgangsstufeSchluessel);

				if (asvjgst != null) {

/*
					jahrgangsstufeValue = ValueDAO.insert(
							ValueListType.form.getKey(), school.getId(),
							asvjgst.getAnzeigeform(), asvjgst.getKurzform(),
							asvjgst.getSchluessel(), 100, con);
*/

					jahrgangsstufeValue = ValueListStore.getInstance().addValue(school.getId(), ValueListType.form, asvjgst.getAnzeigeform(), asvjgst.getKurzform(),
							asvjgst.getSchluessel(), con);

				}
			}

			dbClass = DBClassDAO.insert(schoolTerm.getId(), klasse.klassenname,
					getYearOfSchool(klasse.klassenname),
					jahrgangsstufeValue != null ? jahrgangsstufeValue.getId()
							: null, con);
		}

		List<Student> studentList = StudentDAO.findBySchoolId(school.getId(),
				con);

		for (ASVKlassengruppe klassengruppe : klasse.klassengruppen) {
			for (ASVSchuelerin schuelerin : klassengruppe.schueler) {
				
				schuelerZaehler++;
				if (schuelerZaehler % 5 == 0) {
					publishProgress(
									0,
									220,
									20 + (int) ((double) schuelerZaehler * 180 / (double) schuelerAnzahl),
									"Importiere Schüler/in " + schuelerin.rufname + " "
											+ schuelerin.familienname + ", Klasse "
											+ klasse.klassenname
											+ " in die Datenbank...", false, "",
									progressCode);
				}


				SchuelerWriter sw = new SchuelerWriter(this,schuelerin, klasse, klassengruppe, asvSchule,
						dbClass, schoolTerm, studentList, con);
				
				sw.start();
			}
		}

	}



	private int getYearOfSchool(String klassenname) {

		String jgstString = "";

		for (int i = 0; i < klassenname.length(); i++) {
			if (Character.isDigit(klassenname.charAt(i))) {
				jgstString += klassenname.charAt(i);
			} else {
				break;
			}
		}

		if (jgstString.length() > 0) {
			return Integer.parseInt(jgstString);
		}

		return 4;
	}

	public void protocolAdd(String text, boolean bold, String color,
			int indentation) {
		String boldString = bold ? "; font-weight: bold" : "";

		if (color != null && !color.startsWith("#")) {
			color = "#" + color;
		}

		String colorString = color == null ? "" : "; color: " + color;

		protocol.append("<div style=\"margin-left: " + indentation + "em "
				+ boldString + colorString + "\">");

		protocol.append(text);

		protocol.append("</div>\n");
	}



}
