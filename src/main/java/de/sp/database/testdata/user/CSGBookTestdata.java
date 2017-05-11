package de.sp.database.testdata.user;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BookDAO;
import de.sp.database.daos.basic.BookFormDAO;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.Book;
import de.sp.database.model.Subject;
import de.sp.database.model.Value;
import de.sp.database.valuelists.ValueListType;
import de.sp.database.testdata.valuelists.ASVBildungsgang;
import de.sp.database.testdata.valuelists.ASVJahrgangsstufe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

public class CSGBookTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting book testdata...");


		try (Connection con = ConnectionPool.open()) {

			Long school_id = SchoolTestdata.exampleSchool.getId();

			Subject mathematik = SubjectDAO.findFirstBySchoolIDAndKey1(
					school_id, "0300400100", con);
			Subject englisch = SubjectDAO.findFirstBySchoolIDAndKey1(school_id,
					"0100600100", con);
			@SuppressWarnings("unused")
			Subject deutsch = SubjectDAO.findFirstBySchoolIDAndKey1(school_id,
					"0100500100", con);

			Book book1 = BookDAO.insert(school_id, "Pippi Langstrumpf",
					"Astrid Lindgren", "1111-2222", "Beispielverlag",
					"Bemerkungen", "ED-988-f", "2. Auflage",
					mathematik.getId(), 16.44d, con);
			Book book2 = BookDAO.insert(school_id,
					"Harry Potter and the philosoper's Stone", "J. K. Rowling",
					"2222-3333", "XXXYYY-Verlag", "Bemerkungen2",
					"YN-7899", "1. Auflage",
					englisch.getId(), 20.99d, con);

			Value jgst5 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueListType.form.getKey(),
					ASVJahrgangsstufe.jgst5.getSchluessel(), con);
			Value jgst6 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueListType.form.getKey(),
					ASVJahrgangsstufe.jgst6.getSchluessel(), con);
			Value jgst7 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueListType.form.getKey(),
					ASVJahrgangsstufe.jgst7.getSchluessel(), con);
			Value jgst8 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueListType.form.getKey(),
					ASVJahrgangsstufe.jgst8.getSchluessel(), con);

			Value ntg8 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueListType.curriculum.getKey(),
					ASVBildungsgang.GY_NTG_8.getSchluessel(), con);
			
			Value sg8 = ValueDAO.findBySchoolAndValueStoreAndExternalKey(
					school_id, ValueListType.curriculum.getKey(),
					ASVBildungsgang.GY_SG_8.getSchluessel(), con);

			BookFormDAO.insert(book1.getId(), jgst5.getId(), ntg8.getId(), null, con);
			BookFormDAO.insert(book1.getId(), jgst6.getId(), sg8.getId(), null, con);

			BookFormDAO.insert(book2.getId(), jgst7.getId(), ntg8.getId(), 2, con);
			BookFormDAO.insert(book2.getId(), jgst8.getId(), sg8.getId(), 3, con);
			
			
		}
	}

}
