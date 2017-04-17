package de.sp.database.testdata.student;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.*;
import de.sp.database.model.*;
import de.sp.database.model.valuelists.ValueStore;
import de.sp.database.testdata.user.SchoolTestdata;
import de.sp.database.testdata.valuelists.ASVBildungsgang;
import de.sp.database.testdata.valuelists.ASVJahrgangsstufe;
import de.sp.database.testdata.valuelists.ASVUnterrichtsfach;
import de.sp.database.valuelists.VLSex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.text.SimpleDateFormat;

public class StudentTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting student testdata...");

		try (Connection con = ConnectionPool.open()) {

			School testSchool = SchoolTestdata.exampleSchool;
			Long school_id = testSchool.getId();
			SchoolTerm schoolTerm = SchoolTestdata.exampleSchoolTerm;
			Long school_term_id = schoolTerm.getId();

			Value jgst8 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueStore.form.getKey(),
					ASVJahrgangsstufe.jgst8.getSchluessel(), con);

			Value ntg8 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueStore.curriculum.getKey(),
					ASVBildungsgang.GY_NTG_8.getSchluessel(), con);
			
			Value sg8 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueStore.curriculum.getKey(),
					ASVBildungsgang.GY_SG_8.getSchluessel(), con);
			
			Subject englisch = SubjectDAO.findFirstBySchoolIDAndKey1(school_id,
					ASVUnterrichtsfach.E.getSchluessel(), con);

			Subject franzoesisch = SubjectDAO.findFirstBySchoolIDAndKey1(school_id,
					ASVUnterrichtsfach.F.getSchluessel(), con);
			
			Subject latein = SubjectDAO.findFirstBySchoolIDAndKey1(school_id,
					ASVUnterrichtsfach.L.getSchluessel(), con);

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

			DBClass achtA = DBClassDAO.insert(school_term_id, "8a", 8,
					jgst8.getId(), con);

			Student benno_beispiel = StudentDAO.insert(school_id,
					sdf.parse("03.09.2001"), "Beispiel", "Benno",
					"Benno Ludwig", "von", "auf der Schanz",
					VLSex.male.getKey(), "", null, con);

			StudentSchoolTerm benno_beispiel_st = StudentSchoolTermDAO.insert(
					benno_beispiel.getId(), school_term_id, achtA.getId(),
					ntg8.getId(), con);

			Languageskill lsBennoE = LanguageskillDAO.insert(benno_beispiel.getId(), englisch.getId(), 5, null, con);
			Languageskill lsBennoF = LanguageskillDAO.insert(benno_beispiel.getId(), franzoesisch.getId(), 6, null, con);
			
			Student felicitas_fleissig = StudentDAO.insert(school_id,
					sdf.parse("03.09.2001"), "Fleißig", "Felicitas",
					"", "", "",
					VLSex.female.getKey(), "", null, con);

			StudentSchoolTerm felicitas_fleissig_st = StudentSchoolTermDAO.insert(
					felicitas_fleissig.getId(), school_term_id, achtA.getId(),
					sg8.getId(), con);

			Languageskill lsFelicitasE = LanguageskillDAO.insert(felicitas_fleissig.getId(), englisch.getId(), 5, null, con);
			Languageskill lsFelicitasL = LanguageskillDAO.insert(felicitas_fleissig.getId(), latein.getId(), 6, null, con);

			
			
			
		}
	}

}
