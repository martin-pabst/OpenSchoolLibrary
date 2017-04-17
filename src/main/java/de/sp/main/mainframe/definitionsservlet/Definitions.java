package de.sp.main.mainframe.definitionsservlet;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Subject;
import de.sp.database.model.User;
import de.sp.database.model.Value;
import de.sp.database.model.valuelists.ValueStore;

@SuppressWarnings("unused")
public class Definitions {

	private List<SimpleValueListEntry> formList;

	private List<SimpleValueListEntry> curriculumList;

	private List<SimpleValueListEntry> subjectList;

	private List<School> schoolList;
	
	private String username;
	
	public Definitions(User user, School school, SchoolTerm schoolTerm) {

		Long school_id = school.getId();

		try (Connection con = ConnectionPool.open()) {

			formList = getValueList(school_id, ValueStore.form, con);

			curriculumList = getValueList(school_id, ValueStore.curriculum, con);

			curriculumList.add(new SimpleValueListEntry(null, "Alle"));

			subjectList = getSubjectList(school_id, con);
			
			schoolList = user.getSchools();
			
			username = user.getName();
						
		}
	}

	private List<SimpleValueListEntry> getValueList(Long school_id,
			ValueStore valueStore, Connection con) {

		Long valuestore_key = valueStore.getKey();

		List<Value> list = ValueDAO.findBySchoolAndValueStore(school_id,
				valuestore_key, con);

		ArrayList<SimpleValueListEntry> ret = new ArrayList<>();

		for (Value value : list) {
			ret.add(new SimpleValueListEntry(value.getId(), value
					.getAbbreviation()));
		}

		return ret;
	}

	private List<SimpleValueListEntry> getSubjectList(Long school_id,
			Connection con) {

		List<Subject> list = SubjectDAO.getAll(con);

		ArrayList<SimpleValueListEntry> ret = new ArrayList<>();

		for (Subject subject : list) {
			ret.add(new SimpleValueListEntry(subject.getId(), subject
					.getShortname()));
		}

		return ret;
	}

}
