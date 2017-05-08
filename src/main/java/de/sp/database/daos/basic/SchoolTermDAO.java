package de.sp.database.daos.basic;

import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.Date;
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

	public static SchoolTerm insert(School school, String name, Date begindate, Date enddate, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("school_term.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("school_id", school.getId())
				.addParameter("name", name)
				.addParameter("begindate", begindate)
				.addParameter("enddate", enddate)
				.executeUpdate()
				.getKey(Long.class);

		return new SchoolTerm(id, school, name, begindate, enddate);

	}

	public static void delete(SchoolTerm school_term, Connection con) {

		String sql = StatementStore.getStatement("school_term.delete");

		con.createQuery(sql)

		.addParameter("id", school_term.getId())

		.executeUpdate();

	}
}
