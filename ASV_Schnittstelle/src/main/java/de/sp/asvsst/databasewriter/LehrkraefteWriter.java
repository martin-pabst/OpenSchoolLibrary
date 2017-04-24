package de.sp.asvsst.databasewriter;

import de.sp.asvsst.model.ASVExport;
import de.sp.asvsst.model.ASVLehrkraftSchuleSchuljahr;
import de.sp.asvsst.model.ASVLehrkraftSchuljahr;
import de.sp.asvsst.model.ASVSchule;
import de.sp.asvsst.model.wertelisten.ASVAmtsbezeichnung;
import de.sp.database.daos.basic.TeacherDAO;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Teacher;
import de.sp.database.model.User;
import de.sp.tools.server.progressServlet.ProgressServlet;
import org.sql2o.Connection;

import java.util.HashMap;
import java.util.List;

public class LehrkraefteWriter {
	
	private ASVToDatabaseWriter asvToDBWriter;
	
	private ASVSchule asvSchule;
	private School school;
	private SchoolTerm schoolTerm; 
	private ASVExport asvExport;
	private Connection con;
	private String progressCode;
	

	
	public LehrkraefteWriter(ASVToDatabaseWriter asvToDBWriter,
			ASVSchule asvSchule, School school, SchoolTerm schoolTerm,
			ASVExport asvExport, Connection con, String progressCode) {
		super();
		this.asvToDBWriter = asvToDBWriter;
		this.asvSchule = asvSchule;
		this.school = school;
		this.schoolTerm = schoolTerm;
		this.asvExport = asvExport;
		this.con = con;
		this.progressCode = progressCode;
	}

	private void publishProgress(int min, int max, int now,
								 String text, boolean completed, Object result, String progressCode){


		if (progressCode != null) {
			ProgressServlet.publishProgress(min, max, now, text, completed, result, progressCode);
		} else {
			System.out.println("Stand: " + now + ", " + text);
		}

	}


	public void start() throws Exception{
		int lkZaehler = 0;

		asvToDBWriter.protocolAdd("Importiere Lehrkräfte", true, "0000ff", 2);


		// Reset synchronized-attribute. For all teachers found in ASV this attribute
		// is set again later in this process
		TeacherDAO.setSynchronizedForAll(school.getId(), false, con);

		// Mappt XML-Ids auf die LehrkraftSchuljahr-Elemente
		HashMap<Integer, ASVLehrkraftSchuljahr> asvLehrkraftSchuljahrMap = new HashMap<>();

		for (ASVLehrkraftSchuljahr asvLk : asvExport.lehrkraefte) {
			asvLehrkraftSchuljahrMap.put(asvLk.xml_id, asvLk);
		}

		List<Teacher> teacherList = TeacherDAO
				.findBySchool(school.getId(), con);

		for (ASVLehrkraftSchuleSchuljahr asvLk : asvSchule.lehrkraefte) {

			ASVLehrkraftSchuljahr lk = asvLehrkraftSchuljahrMap
					.get(asvLk.lehrkraftdaten_nicht_schulbezogen_id);

			lkZaehler++;

			if (lkZaehler % 3 == 0) {
				publishProgress(
								0,
								220,
								200 + (int) ((double) lkZaehler * 20 / (double) asvSchule.lehrkraefte
										.size()), "Importiere " + lk.rufname
										+ " " + lk.familienname
										+ " in die Datenbank...", false, "",
										progressCode);
			}

			asvToDBWriter.protocolAdd(lk.rufname + " " + lk.familienname, false, "000000", 3);

			Teacher teacher = null;

			for (Teacher t : teacherList) {

				if (t.getExternal_id() != null
						&& t.getExternal_id().equals(
								lk.lokales_differenzierungsmerkmal)) {
					teacher = t;
					break;
				}

				if (t.getFirstname().equals(lk.rufname)
						&& t.getSurname().equals(lk.familienname)) {
					teacher = t;
					break;
				}
			}

			ASVAmtsbezeichnung az = ASVAmtsbezeichnung
					.findBySchluessel(lk.amtsbezeichnungSchluessel);
			String azKurzform = "";
			if (az != null) {
				azKurzform = az.getKurzform();
			}

			if (teacher == null) {

				User user = UserDAO
						.insert(lk.rufname + "." + lk.familienname, lk.rufname
								+ " " + lk.familienname, "!234%a", "de", null, false, con);
				teacher = TeacherDAO.insert(school.getId(), user.getId(),
						lk.familienname, lk.rufname,
						lk.namensbestandteil_vorangestellt,
						lk.namensbestandteil_nachgestellt, asvLk.namenskuerzel,
						lk.lokales_differenzierungsmerkmal, azKurzform, true, con);
			} else {
				teacher.setSurname(lk.familienname);
				teacher.setFirstname(lk.rufname);
				teacher.setAbbreviation(asvLk.namenskuerzel);
				teacher.setGrade(azKurzform);
				teacher.setExternal_id(lk.lokales_differenzierungsmerkmal);
				teacher.setSynchronized(true);

				TeacherDAO.update(teacher, con);
			}

		}
	}
	
}
