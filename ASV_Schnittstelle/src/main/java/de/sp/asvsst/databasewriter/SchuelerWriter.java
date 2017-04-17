package de.sp.asvsst.databasewriter;

import java.util.List;

import org.sql2o.Connection;

import de.sp.asvsst.model.ASVFremdsprache;
import de.sp.asvsst.model.ASVKlasse;
import de.sp.asvsst.model.ASVKlassengruppe;
import de.sp.asvsst.model.ASVSchuelerin;
import de.sp.asvsst.model.ASVSchule;
import de.sp.asvsst.model.wertelisten.ASVBildungsgang;
import de.sp.asvsst.model.wertelisten.ASVGeschlecht;
import de.sp.asvsst.model.wertelisten.ASVUnterrichtsfach;
import de.sp.database.daos.basic.LanguageskillDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.StudentSchoolTermDAO;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.DBClass;
import de.sp.database.model.Languageskill;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Student;
import de.sp.database.model.StudentSchoolTerm;
import de.sp.database.model.Subject;
import de.sp.database.model.Value;
import de.sp.database.model.valuelists.ValueStore;
import de.sp.database.valuelists.VLSex;
import de.sp.tools.objects.ObjectTool;

public class SchuelerWriter {

	private ASVToDatabaseWriter asvToDBWriter;
	private ASVSchuelerin schuelerin;
	private ASVKlasse klasse;
	private ASVKlassengruppe klassengruppe;
	private ASVSchule asvSchule;
	private DBClass dbClass;
	private SchoolTerm schoolTerm;
	private List<Student> studentList;
	private Connection con;

	public SchuelerWriter(ASVToDatabaseWriter asvToDBWriter,
			ASVSchuelerin schuelerin, ASVKlasse klasse,
			ASVKlassengruppe klassengruppe, ASVSchule asvSchule,
			DBClass dbClass, SchoolTerm schoolTerm, List<Student> studentList,
			Connection con) {
		super();
		this.asvToDBWriter = asvToDBWriter;
		this.schuelerin = schuelerin;
		this.klasse = klasse;
		this.klassengruppe = klassengruppe;
		this.asvSchule = asvSchule;
		this.dbClass = dbClass;
		this.schoolTerm = schoolTerm;
		this.studentList = studentList;
		this.con = con;
	}

	public void start() throws Exception {

		// Gibt es eine Schülerin mit gegebenem ASV-Merkmal bzw. Namen und
		// Geburtsdatum in diesem Schuljahr?

		Student student = null;

		for (Student student1 : studentList) {

			String external_id = student1.getExternal_id();

			if (external_id != null
					&& external_id
							.equals(schuelerin.lokales_differenzierungsmerkmal)) {
				student = student1;
				break;
			}

			if (student1.getDateofbirth() != null
					&& student1.getDateofbirth()
							.equals(schuelerin.geburtsdatum)) {
				if (student1.getFirstname() != null
						&& student1.getFirstname().equals(schuelerin.rufname)) {
					if (student1.getSurname() != null
							&& student1.getSurname().equals(
									schuelerin.familienname)) {
						student = student1;
						break;
					}
				}
			}

		}

		ASVGeschlecht geschlecht = schuelerin.getGeschlecht();

		VLSex sex = VLSex.male;

		if (geschlecht == ASVGeschlecht.W) {
			sex = VLSex.female;
		}

		if (student == null) {

			asvToDBWriter.protocolAdd("Lege Datensatz für Schüler/in "
					+ schuelerin.rufname + " " + schuelerin.familienname
					+ " an.", false, "#0000ff", 3);

			student = StudentDAO.insert(schoolTerm.getSchool().getId(),
					schuelerin.geburtsdatum, schuelerin.familienname,
					schuelerin.rufname, schuelerin.vornamen,
					schuelerin.namensbestandteil_vorangestellt,
					schuelerin.namensbestandteil_nachgestellt, sex.getKey(),
					schuelerin.lokales_differenzierungsmerkmal,
					schuelerin.austrittsdatum, con);
		} else {

			// TODO: eigentlich könnte man hier vorher überprüfen, ob wirklich
			// ein update nötig ist...
			student.setDateofbirth(schuelerin.geburtsdatum);
			student.setSurname(schuelerin.familienname);
			student.setFirstname(schuelerin.rufname);
			student.setChristian_names(schuelerin.vornamen);
			student.setBefore_surname(schuelerin.namensbestandteil_vorangestellt);
			student.setAfter_surname(schuelerin.namensbestandteil_nachgestellt);
			student.setSex(sex);
			student.setExternal_id(schuelerin.lokales_differenzierungsmerkmal);
			student.setExit_date(schuelerin.austrittsdatum);

			StudentDAO.update(student, con);

		}

		Long school_id = schoolTerm.getSchool().getId();
		ASVBildungsgang asvBildungsgang = klassengruppe.getBildungsgang();

		Long bildungsgang_id = null;

		if (asvBildungsgang != null) {
			// Wertelisteneintrag für den Bildungsgang holen
			Value bildungsgangValue = ValueDAO
					.findBySchoolAndValueStoreAndExternalKey(school_id,
							ValueStore.curriculum.getKey(),
							asvBildungsgang.getSchluessel(), con);

			if (bildungsgangValue == null) {
				bildungsgangValue = ValueDAO.insert(
						ValueStore.curriculum.getKey(), school_id,
						asvBildungsgang.getAnzeigeform(),
						asvBildungsgang.getKurzform(),
						asvBildungsgang.getSchluessel(), con);
			}

			bildungsgang_id = bildungsgangValue.getId();
		}

		// Eintrag in StudentSchoolTerm ggf. anlegen
		StudentSchoolTerm studentSchoolTerm = null;

		List<StudentSchoolTerm> studentSchoolTerms = StudentSchoolTermDAO
				.findByStudentAndSchoolTerm(student.getId(),
						schoolTerm.getId(), con);

		if (studentSchoolTerms.size() == 1) {

			studentSchoolTerm = studentSchoolTerms.get(0);

		} else {

			studentSchoolTerm = StudentSchoolTermDAO.insert(student.getId(),
					schoolTerm.getId(), dbClass.getId(), bildungsgang_id, con);

		}

		// Verbindung zur Klasse ggf. anlegen bzw. erneuern
		if (!(ObjectTool.equalsOrBothNull(studentSchoolTerm.getClass_id(),
				dbClass.getId()) && ObjectTool.equalsOrBothNull(
				studentSchoolTerm.getCurriculum_id(), bildungsgang_id))) {

			studentSchoolTerm.setCurriculum_id(bildungsgang_id);
			studentSchoolTerm.setClass_id(dbClass.getId());

			StudentSchoolTermDAO.update(studentSchoolTerm, con);

		}

		importFremdsprachenfolge(student, school_id, schuelerin, asvSchule, con);
		
		SchuelerAnschriftenImporter.start(asvToDBWriter, student, school_id,
				schuelerin, con);

	}

	private void importFremdsprachenfolge(Student student, Long school_id,
			ASVSchuelerin schuelerin, ASVSchule asvSchule, Connection con)
			throws Exception {

		List<Languageskill> languageSkills = LanguageskillDAO.findByStudentID(
				student.getId(), con);

		for (Languageskill l : languageSkills) {
			LanguageskillDAO.delete(l, con);
		}

		for (ASVFremdsprache asvFremdsprache : schuelerin.fremdsprachen) {

			Subject subject = SubjectDAO.findFirstBySchoolIDAndKey1(school_id,
					asvFremdsprache.unterrichtsfachSchluessel, con);

			if (subject == null) {
				ASVUnterrichtsfach asvUFach = asvFremdsprache
						.getUnterrichtsfach();
				subject = SubjectDAO.insert(asvUFach.getKurzform(),
						asvUFach.getAnzeigeform(), school_id,
						asvUFach.getSchluessel(), null, con);
			}

			Integer vonJgst = asvFremdsprache.getVonJahrgangsstufeInt();
			Integer bisJgst = asvFremdsprache.getBisJahrgangsstufeInt();

			LanguageskillDAO.insert(student.getId(), subject.getId(), vonJgst,
					bisJgst, con);

		}

	}

}
