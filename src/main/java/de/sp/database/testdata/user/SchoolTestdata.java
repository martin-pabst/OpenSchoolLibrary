package de.sp.database.testdata.user;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SchoolDAO;
import de.sp.database.daos.basic.SchoolTermDAO;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import org.apache.lucene.index.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.text.SimpleDateFormat;

public class SchoolTestdata {

	public static School exampleSchool;
	public static SchoolTerm exampleSchoolTerm;
	
	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting school testdata...");


		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		
		try (Connection con = ConnectionPool.open()) {

		exampleSchool = SchoolDAO.insert("Christoph-Scheiner-Gymnasium", "0124", "CSG", con);
		
		exampleSchoolTerm = SchoolTermDAO.insert(exampleSchool, "2016/17", sdf.parse("01.08.2016"), sdf.parse("31.07.2017"), con);
		
		}
	}

}
