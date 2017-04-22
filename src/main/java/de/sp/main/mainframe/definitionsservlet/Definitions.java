package de.sp.main.mainframe.definitionsservlet;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.DBClassDAO;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.*;
import de.sp.database.model.valuelists.ValueStore;
import de.sp.database.valuelists.VLSex;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Definitions {

	private List<SimpleValueListEntry> formList;

	private List<SimpleValueListEntry> curriculumList;

	private List<SimpleValueListEntry> subjectList;

	private List<School> schoolList;

	private List<SimpleValueListEntry> classList;

	private List<SimpleValueListEntry> sexList;

	private String username;
	
	public Definitions(User user, School school, SchoolTerm schoolTerm) {

		Long school_id = school.getId();

		try (Connection con = ConnectionPool.open()) {



			formList = getValueList(school_id, ValueStore.form, con);

			curriculumList = getValueList(school_id, ValueStore.curriculum, con);

			sexList = VLSex.getAsValueList();

			curriculumList.add(new SimpleValueListEntry(null, "Alle"));

			subjectList = getSubjectList(school_id, con);
			
			schoolList = user.getSchools();

			classList = DBClassDAO.getSimpleValueList(schoolTerm.getId(), con);

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
