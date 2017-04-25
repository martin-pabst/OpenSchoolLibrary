package de.sp.database.daos.basic;

import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Term;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;
import java.util.Map;

public class SchoolTermDAO {

	public static List<SchoolTerm> getAll(Connection con) {

		String sql = StatementStore.getStatement("school_term.getAll");

		List<SchoolTerm> schoolTermlist = con.createQuery(sql).executeAndFetch(
				SchoolTerm.class);

		return schoolTermlist;

	}

	public static SchoolTerm findBySchoolIdAndTermName(Connection con, Long school_id, String term_name) {

		String sql = StatementStore.getStatement("school_term.findBySchoolIdAndTermName");

		List<SchoolTerm> schoolTermlist = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("term_name", term_name)
				.executeAndFetch(SchoolTerm.class);

		if(schoolTermlist.size() == 0){
			return null;
		}

		return schoolTermlist.get(0);

	}

	/*
		<statement name="school_term.findBySchoolIdAndTermName" >
	select st.id, st.school_id, st.term_id from school
	join school_term st on school.id = st.school_id
	join term on school_term.term_id = term.id
	WHERE school_id = :school_id and term.name = :term_name
			</statement>
*/


	public static void joinSchoolsWithTerms(Map<Long, School> schools,
			Map<Long, Term> terms, Connection con) {

		List<SchoolTerm> schoolTerms = getAll(con);

		for (SchoolTerm st : schoolTerms) {

			School school = schools.get(st.school_id);
			Term term = terms.get(st.term_id);

			if (school != null && term != null) {

				st.setSchool(school);
				st.setTerm(term);

				school.addSchoolTerm(st);
			}

		}

	}

	public static SchoolTerm insert(Long school_id, Long term_id, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("school_term.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("school_id", school_id)
				.addParameter("term_id", term_id).executeUpdate()
				.getKey(Long.class);

		return new SchoolTerm(id, school_id, term_id);

	}

	public static void delete(SchoolTerm school_term, Connection con) {

		String sql = StatementStore.getStatement("school_term.delete");

		con.createQuery(sql)

		.addParameter("id", school_term.getId())

		.executeUpdate();

	}
}
