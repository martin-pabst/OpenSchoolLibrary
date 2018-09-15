package de.sp.main.mainframe.definitionsservlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.DBClassDAO;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.model.*;
import de.sp.database.stores.SchoolTermStore;
import de.sp.database.stores.ValueListStore;
import de.sp.database.valuelists.VLSex;
import de.sp.database.valuelists.ValueListType;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

	private Set<String> permissions;

	private JsonObject librarysettings;
	
	public Definitions(User user, School school, SchoolTerm schoolTerm) {

		// user root has no school
		if (school != null) {

			Long school_id = school.getId();

			try (Connection con = ConnectionPool.open()) {

                formList = getValueList(school_id, ValueListType.form, con);

                curriculumList = getValueList(school_id, ValueListType.curriculum, con);

                sexList = VLSex.getAsValueList();

                curriculumList.add(new SimpleValueListEntry(null, "Alle"));

                getSubjectList(school_id, con);

                schoolList = Arrays.asList(SchoolTermStore.getInstance().getSchoolById(user.getSchool_id()));

                classList = DBClassDAO.getSimpleValueList(schoolTerm.getId(), con);

                permissions = user.getPermissions();

                String librarySettingsStr = school.getLibrarysettings();
                if(librarySettingsStr == null || librarySettingsStr.isEmpty()){
                	librarySettingsStr = "{}";

				}

				Gson gson = new Gson();
				librarysettings = new JsonParser().parse(librarySettingsStr).getAsJsonObject();

            }
		}

		username = user.getName();

	}

	private List<SimpleValueListEntry> getValueList(Long school_id,
													ValueListType valueStore, Connection con) {

		Long valuestore_key = valueStore.getKey();

/*
		List<Value> list = ValueDAO.findBySchoolAndValueStore(school_id,
				valuestore_key, con);
*/
		List<Value> list = ValueListStore.getInstance().getValueList(school_id,
				valuestore_key);

		ArrayList<SimpleValueListEntry> ret = new ArrayList<>();

		for (Value value : list) {
			ret.add(new SimpleValueListEntry(value.getId(), value
					.getAbbreviation()));
		}

		return ret;
	}

	private void getSubjectList(Long school_id,
			Connection con) {

		List<Subject> list = SubjectDAO.getAllForSchool(con, school_id);

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
