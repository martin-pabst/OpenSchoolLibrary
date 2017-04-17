package de.sp.database.stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SchoolDAO;
import de.sp.database.daos.basic.SchoolTermDAO;
import de.sp.database.daos.basic.TermDAO;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Term;

public class SchoolTermStore {

	private List<School> schools = new ArrayList<>();
	private Map<Long, School> schoolKeys = new HashMap<>();

	private List<Term> terms = new ArrayList<>();
	private Map<Long, Term> termKeys = new HashMap<>();
	
	private Map<Long, SchoolTerm> schoolTermIds = new HashMap<>();

	private static SchoolTermStore instance;

	private SchoolTermStore() {

	}

	public static SchoolTermStore getInstance() {

		if (instance == null) {

			instance = new SchoolTermStore();

		}

		return instance;
	}

	public List<School> getSchools() {
		return schools;
	}

	public Map<Long, School> getSchoolKeys() {
		return schoolKeys;
	}

	public void loadFromDatabase() {

		try (Connection con = ConnectionPool.open()) {

			schools = SchoolDAO.getAll(con);
			schoolKeys = SchoolDAO.schoolListToKeyMap(schools);

			terms = TermDAO.getAll(con);
			termKeys = TermDAO.termListToKeyMap(terms);

			SchoolTermDAO.joinSchoolsWithTerms(schoolKeys, termKeys, con);
			
			for(School school: schools){
				for(SchoolTerm st: school.getSchoolTerms()){
					schoolTermIds.put(st.getId(), st);
				}
			}
		}

	}

	public School getSchoolById(Long school_id) {
		return schoolKeys.get(school_id);
	}

	public Term getTerm(String termName) {
		for (Term term : terms) {
			if (termName.equals(term.getName())) {
				return term;
			}
		}
		return null;
	}

	public void addTerm(Term term) {

		terms.add(term);
		termKeys.put(term.getId(), term);

	}


	public SchoolTerm getSchoolTerm(Long schoolTermId) {
		return schoolTermIds.get(schoolTermId);
	}

	public void addSchoolTerm(SchoolTerm schoolTerm) {
		schoolTermIds.put(schoolTerm.getId(), schoolTerm);
	}

}
