package de.sp.asvsst.databasewriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import de.sp.asvsst.database.daos.ASVSStDAO;
import de.sp.asvsst.model.ASVExport;
import de.sp.asvsst.model.ASVKlasse;
import de.sp.asvsst.model.ASVKlassengruppe;
import de.sp.asvsst.model.ASVSchuelerin;
import de.sp.asvsst.model.ASVSchule;
import de.sp.asvsst.model.asvwlstore.ASVWlStore;
import de.sp.asvsst.model.wertelisten.ASVJahrgangsstufe;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.DBClassDAO;
import de.sp.database.daos.basic.SchoolTermDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.TermDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.DBClass;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Student;
import de.sp.database.model.Term;
import de.sp.database.model.Value;
import de.sp.database.model.valuelists.ValueStore;
import de.sp.database.stores.SchoolTermStore;
import de.sp.main.StartServer;
import de.sp.tools.server.progressServlet.ProgressServlet;

public class ASVToDatabaseWriter {

	private StringBuilder protocol;
	private String progressCode;
	private Logger logger;

	private int schuelerZaehler;
	private int schuelerAnzahl;

	public ASVToDatabaseWriter(String progressCode) {
		super();
		this.progressCode = progressCode;
		logger = LoggerFactory.getLogger(StartServer.class);
	}

	public void writeToDatabase(ASVExport asvExport) {

		protocol = new StringBuilder();

		ProgressServlet.publishProgress(0, 120, 20,
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
		
		ProgressServlet.publishProgress(0, 120, 20, "Fertig!", true, result
				+ protocol.toString(), progressCode);

	}

	private void importSchool(ASVSchule asvSchule, School school,
			ASVExport asvExport, Connection con) throws Exception {

		ProgressServlet.publishProgress(0, 120, 20,
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
		Term term = SchoolTermStore.getInstance().getTerm(asvSchule.schuljahr);

		if (term == null) {

			String jahr1String = asvSchule.schuljahr.substring(0, 4);
			int jahr1 = Integer.parseInt(jahr1String);
			int jahr2 = jahr1 + 1;

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date begin;

			begin = sdf.parse("01.08." + jahr1);
			Date end = sdf.parse("31.07." + jahr2);
			protocolAdd("Lege Schuljahr " + asvSchule.schuljahr
					+ " in der Datenbank an.\n", false, "#0000ff", 1);
			term = TermDAO.insert(asvSchule.schuljahr, begin, end, con);

			SchoolTermStore.getInstance().addTerm(term);
		}

		SchoolTerm schoolTerm = null;

		for (SchoolTerm schoolTerm1 : school.getSchoolTerms()) {
			if (schoolTerm1.getTerm() == term) {
				schoolTerm = schoolTerm1;
				break;
			}
		}

		if (schoolTerm == null) {
			protocolAdd("Lege Relation von Schule " + school.getNumber()
					+ " zu Schuljahr " + term.getName()
					+ " in der Datenbank an.", false, "#0000ff", 1);
			schoolTerm = SchoolTermDAO
					.insert(school.getId(), term.getId(), con);
			school.addSchoolTerm(schoolTerm);
			SchoolTermStore.getInstance().addSchoolTerm(schoolTerm);
			schoolTerm.setTerm(term);
			schoolTerm.setSchool(school);
		}

		return schoolTerm;
	}

	private void importKlasse(ASVKlasse klasse, ASVSchule asvSchule,
			School school, HashMap<String, DBClass> classMap,
			SchoolTerm schoolTerm, Connection con) throws Exception {

		DBClass dbClass = classMap.get(klasse.klassenname);

		if (dbClass == null) {

			protocolAdd("Lege Klasse " + klasse.klassenname + " im Schuljahr "
					+ schoolTerm.getTerm().getName() + " an.", true, "#000000",
					2);

			String jahrgangsstufeSchluessel = "051";

			if (klasse.klassengruppen.size() > 0) {
				ASVKlassengruppe kg = klasse.klassengruppen.get(0);
				jahrgangsstufeSchluessel = kg.jahrgangsstufeSchluessel;
			}

			// Wertelisteneintrag für die Jahrgangsstufe holen
			Value jahrgangsstufeValue = ValueDAO
					.findBySchoolAndValueStoreAndExternalKey(school.getId(),
							ValueStore.form.getKey(), jahrgangsstufeSchluessel,
							con);

			if (jahrgangsstufeValue == null) {

				ASVJahrgangsstufe asvjgst = ASVJahrgangsstufe
						.findBySchluessel(jahrgangsstufeSchluessel);

				if (asvjgst != null) {

					jahrgangsstufeValue = ValueDAO.insert(
							ValueStore.form.getKey(), school.getId(),
							asvjgst.getAnzeigeform(), asvjgst.getKurzform(),
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
					ProgressServlet
							.publishProgress(
									0,
									220,
									20 + (int) ((double) schuelerZaehler * 100 / (double) schuelerAnzahl),
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
