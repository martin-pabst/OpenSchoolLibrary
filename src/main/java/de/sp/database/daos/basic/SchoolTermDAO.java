package de.sp.database.daos.basic;

import java.util.List;
import java.util.Map;

import org.sql2o.Connection;

import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.Term;
import de.sp.database.statements.StatementStore;

public class SchoolTermDAO {

	public static List<SchoolTerm> getAll(Connection con) {

		String sql = StatementStore.getStatement("school_term.getAll");

		List<SchoolTerm> schoolTermlist = con.createQuery(sql).executeAndFetch(
				SchoolTerm.class);

		return schoolTermlist;

	}

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
