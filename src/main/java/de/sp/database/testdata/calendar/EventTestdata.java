package de.sp.database.testdata.calendar;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.Absence;
import de.sp.database.model.Event;
import de.sp.database.model.EventRestriction;
import de.sp.database.model.School;
import de.sp.database.stores.EventStore;
import de.sp.database.testdata.student.StudentTestdata;
import de.sp.database.testdata.user.SchoolTestdata;
import de.sp.database.testdata.user.UserTestdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.text.SimpleDateFormat;

public class EventTestdata {

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting user testdata...");


		try (Connection con = ConnectionPool.open()) {

			School testSchool = SchoolTestdata.exampleSchool;
			Long school_id = testSchool.getId();

			EventStore cs = new EventStore();

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

			// Simple entry, visible by all users
			Event eventPublic1 = new Event(school_id, "Christmas feast", "Marry Christmas!", "Christmas", "Bethlehem", true,
					false, sdf.parse("24.12.2017"), sdf.parse("24.12.2017"), null, null, "#a00000", null, null, "#000");
			cs.storeEventIntoDatabase(eventPublic1, con);

			Event eventPublic2 = new Event(school_id, "Januar Weeks", "Two weeks in january", "Jan-Weeks", "Warm room at home", true,
					false, sdf.parse("03.01.2018"), sdf.parse("17.01.2018"), null, null, "#0a0", null, null, "#fff");
			cs.storeEventIntoDatabase(eventPublic2, con);

			Event eventPublic3 = new Event(school_id, "School", "One School day in february", "Feb-School", "CSG Ingolstadt", false,
					false, sdfTime.parse("04.02.2018 08:00:00"), sdfTime.parse("04.02.2018 13:00:00"), null, null, "#00a", null, null, "#000");
			cs.storeEventIntoDatabase(eventPublic3, con);

			Event teacherCal = new Event(school_id, "Only teachers", "This is only for teachers", "April - Teachers", "Teachers' room", true,
					false, sdf.parse("14.04.2018"), sdf.parse("15.04.2018"), null, null, "#0a0", null, null, "#ffffff");
			teacherCal.addRestriction(new EventRestriction(school_id, UserTestdata.teacherRole.getId(), null, teacherCal));
			cs.storeEventIntoDatabase(teacherCal, con);

			Event absenceEvent = new Event(school_id, "Absence 8A", "class 8A is absent", "8a theatre", "Communal theatre", false,
					false, sdf.parse("16.04.2018 09:00"), sdf.parse("16.04.2018 11:30"), null, null, "#44c", null, null, "#000");

			Absence absence8A = new Absence(school_id, StudentTestdata.achtA.getId(), null, true, true);

			absenceEvent.addAbsence(absence8A);
			cs.storeEventIntoDatabase(absenceEvent, con);

		}
	}
}
