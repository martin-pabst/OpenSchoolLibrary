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
					false, sdf.parse("24.12.2017"), sdf.parse("24.12.2017"),  "#a00000", "#a00000","#000", false);
			cs.storeEventWithAbsencesAndRestrictionsIntoDatabaseAndStore(eventPublic1, con, false);

			Event eventPublic2 = new Event(school_id, "Januar Weeks", "Two weeks in january", "Jan-Weeks", "Warm room at home", true,
					false, sdf.parse("03.01.2018"), sdf.parse("17.01.2018"), "#0a0", "#0a0", "#fff", false);
			cs.storeEventWithAbsencesAndRestrictionsIntoDatabaseAndStore(eventPublic2, con, false);

			Event eventPublic3 = new Event(school_id, "School", "One School day in february", "Feb-School", "CSG Ingolstadt", false,
					false, sdfTime.parse("04.02.2018 08:00:00"), sdfTime.parse("04.02.2018 13:00:00"), "#00a","#00a", "#000", false);
			cs.storeEventWithAbsencesAndRestrictionsIntoDatabaseAndStore(eventPublic3, con, false);

			Event teacherCal = new Event(school_id, "Only teachers", "This is only for teachers", "April - Teachers", "Teachers' room", true,
					false, sdf.parse("14.04.2018"), sdf.parse("15.04.2018"), "#0a0", "#0a0", "#ffffff", false);
			teacherCal.addRestriction(new EventRestriction(UserTestdata.teacherRole.getId(), teacherCal));
			cs.storeEventWithAbsencesAndRestrictionsIntoDatabaseAndStore(teacherCal, con, false);

			Event absenceEvent = new Event(school_id, "Absence 8A", "class 8A is absent", "8a theatre", "Communal theatre", false,
					false, sdf.parse("16.04.2018 09:00"), sdf.parse("16.04.2018 11:30"), "#44c", "#44c", "#000", false);

			Absence absence8A = new Absence(school_id, StudentTestdata.achtA.getId(), null, true, true);

			absenceEvent.addAbsence(absence8A);
			cs.storeEventWithAbsencesAndRestrictionsIntoDatabaseAndStore(absenceEvent, con, false);

		}
	}
}
