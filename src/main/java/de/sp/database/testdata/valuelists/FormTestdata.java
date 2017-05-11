package de.sp.database.testdata.valuelists;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.valuelists.ValueListType;
import de.sp.database.testdata.user.SchoolTestdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

public class FormTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting form testdata...");


		try (Connection con = ConnectionPool.open()) {

			int i = 0;

			for (ASVJahrgangsstufe asvJgst : ASVJahrgangsstufe.values()) {

				ValueDAO.insert(ValueListType.form.getKey(),
						SchoolTestdata.exampleSchool.getId(),
						asvJgst.getAnzeigeform(), asvJgst.getKurzform(),
						asvJgst.getSchluessel(), i++, con);

			}

		}
	}

}
