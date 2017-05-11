package de.sp.main.mainframe.definitionsservlet;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.DBClassDAO;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.*;
import de.sp.database.valuelists.ValueListType;
import de.sp.database.stores.SchoolTermStore;
import de.sp.database.valuelists.VLSex;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Definitions {

	private List<SimpleValueListEntry> formList;

	private List<SimpleValueListEntry> curriculumList;

	private List<SimpleValueListEntry> subjectList;

	private List<SimpleValueListEntry> languageList;

	private List<SimpleValueListEntry> religionList;

	private List<School> schoolList;

	private List<SimpleValueListEntry> classList;

	private List<SimpleValueListEntry> sexList;

	private String username;
	
	public Definitions(User user, School school, SchoolTerm schoolTerm) {

		Long school_id = school.getId();

		try (Connection con = ConnectionPool.open()) {



			formList = getValueList(school_id, ValueListType.form, con);

			curriculumList = getValueList(school_id, ValueListType.curriculum, con);

			sexList = VLSex.getAsValueList();

			curriculumList.add(new SimpleValueListEntry(null, "Alle"));

			getSubjectList(school_id, con);
			
			schoolList = Arrays.asList(SchoolTermStore.getInstance().getSchoolById(user.getSchool_id()));

			classList = DBClassDAO.getSimpleValueList(schoolTerm.getId(), con);

			username = user.getName();
						
		}
	}

	private List<SimpleValueListEntry> getValueList(Long school_id,
													ValueListType valueStore, Connection con) {

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

	private void getSubjectList(Long school_id,
			Connection con) {

		List<Subject> list = SubjectDAO.getAll(con, school_id);

		subjectList = new ArrayList<>();
		languageList = new ArrayList<>();
		religionList = new ArrayList<>();

		for (Subject subject : list) {
			SimpleValueListEntry sve = new SimpleValueListEntry(subject.getId(), subject
					.getShortname());

			subjectList.add(sve);

			if(subject.isIs_language()){
				languageList.add(sve);
			}

			if(subject.is_religion()){
				religionList.add(sve);
			}
		}
	}

}
