package de.sp.database.testdata.calendar;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.Calendar;
import de.sp.database.model.CalendarRestriction;
import de.sp.database.model.School;
import de.sp.database.stores.CalendarStore;
import de.sp.database.testdata.user.SchoolTestdata;
import de.sp.database.testdata.user.UserTestdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.text.SimpleDateFormat;

public class CalendarTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting user testdata...");


		try (Connection con = ConnectionPool.open()) {

			School testSchool = SchoolTestdata.exampleSchool;
			Long school_id = testSchool.getId();

			CalendarStore cs = new CalendarStore();

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

			// Simple entry, visible by all users
			Calendar calendarPublic1 = new Calendar(school_id, "Christmas feast", "Marry Christmas!", "Christmas", "Bethlehem", true,
					false, sdf.parse("24.12.2017"), sdf.parse("24.12.2017"), null, null, "#a00000", null, null, "#000");

			Calendar calendarPublic2 = new Calendar(school_id, "Januar Weeks", "Two weeks in january", "Jan-Weeks", "Warm room at home", true,
					false, sdf.parse("03.01.2018"), sdf.parse("17.01.2018"), null, null, "#0a0", null, null, "#fff");

			Calendar calendarPublic3 = new Calendar(school_id, "School", "One School day in february", "Feb-School", "CSG Ingolstadt", false,
					false, sdfTime.parse("04.02.2018 08:00:00"), sdfTime.parse("04.02.2018 13:00:00"), null, null, "#00a", null, null, "#000");

			Calendar teacherCal = new Calendar(school_id, "Only teachers", "This is only for teachers", "April - Teachers", "Teachers' room", true,
					false, sdf.parse("14.04.2018"), sdf.parse("15.04.2018"), null, null, "#0a0", null, null, "#fff");

			teacherCal.addRestriction(new CalendarRestriction(school_id, UserTestdata.teacherRole.getId(), null, teacherCal));

			cs.storeCalendarIntoDatabase(calendarPublic1, con);

			cs.storeCalendarIntoDatabase(calendarPublic2, con);

			cs.storeCalendarIntoDatabase(calendarPublic3, con);

			cs.storeCalendarIntoDatabase(teacherCal, con);


		}
	}
}
