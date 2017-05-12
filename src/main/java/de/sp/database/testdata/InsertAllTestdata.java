package de.sp.database.testdata;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.statements.StatementStore;
import de.sp.database.testdata.calendar.CalendarTestdata;
import de.sp.database.testdata.student.StudentTestdata;
import de.sp.database.testdata.user.BookTestdata;
import de.sp.database.testdata.user.SchoolTestdata;
import de.sp.database.testdata.user.UserTestdata;
import de.sp.database.testdata.valuelists.CurriculumTestdata;
import de.sp.database.testdata.valuelists.FormTestdata;
import de.sp.database.testdata.valuelists.SubjectTestdata;
import de.sp.main.config.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;

//@RunWith(Suite.class)
//@SuiteClasses({ DatabaseSetupTest.class, SchoolTestdata.class, FormTestdata.class,
//		SubjectTestdata.class, CurriculumTestdata.class, UserTestdata.class, BookTestdata.class,
//		StudentTestdata.class})
public class InsertAllTestdata {

	public static void main(String[] args) throws Exception {
		setup();

		new DatabaseSetupTest().testSetupDatabase();
		new SchoolTestdata().test();
		new FormTestdata().test();
		new SubjectTestdata().test();
		new CurriculumTestdata().test();
		new UserTestdata().test();
		new BookTestdata().test();
		new StudentTestdata().test();
		new CalendarTestdata().test();

		teardown();
	}

	@BeforeClass
	public static void setup() throws Exception {
		// Konfigurationsdatei configuration.xml einlesen
		Configuration config = Configuration.getInstance();

		StatementStore.readStatements(config.getDatabase().getType());

		ConnectionPool.init(config);

	}

	@AfterClass
	public static void teardown() {
		ConnectionPool.close();
	}

}
