package de.sp.database.testdata.valuelists;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.valuelists.ValueStore;
import de.sp.database.testdata.user.SchoolTestdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

public class CurriculumTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting testdata...");


		try (Connection con = ConnectionPool.open()) {

			for (ASVBildungsgang asvBildungsgang : ASVBildungsgang.values()) {

				ValueDAO.insert(ValueStore.curriculum.getKey(),
						SchoolTestdata.exampleSchool.getId(),
						asvBildungsgang.getAnzeigeform(),
						asvBildungsgang.getKurzform(),
						asvBildungsgang.getSchluessel(), con);

			}

		}
	}

}
