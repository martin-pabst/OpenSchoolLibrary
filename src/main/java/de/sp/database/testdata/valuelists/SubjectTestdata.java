package de.sp.database.testdata.valuelists;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.testdata.user.SchoolTestdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

public class SubjectTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting subject testdata...");


		try (Connection con = ConnectionPool.open()) {

			for (ASVUnterrichtsfach asvFach : ASVUnterrichtsfach.values()) {

				SubjectDAO.insert(asvFach.getKurzform(),
						asvFach.getAnzeigeform(),
						SchoolTestdata.exampleSchool.getId(),
						asvFach.getSchluessel(), null, con);

			}

		}
	}

}
