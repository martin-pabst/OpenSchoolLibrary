package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.DBClass;
import de.sp.database.statements.StatementStore;

public class DBClassDAO {
	public static List<DBClass> getAll(Connection con) {

		String sql = StatementStore.getStatement("class.getAll");
		List<DBClass> classlist = con.createQuery(sql).executeAndFetch(
				DBClass.class);
		return classlist;

	}

	public static DBClass insert(Long school_term_id, String name,
			Integer year_of_school, Long form_id, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("class.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("school_term_id", school_term_id)
				.addParameter("name", name)
				.addParameter("year_of_school", year_of_school)
				.addParameter("form_id", form_id).executeUpdate()
				.getKey(Long.class);

		return new DBClass(id, school_term_id, name, year_of_school, form_id);

	}

	public static void delete(DBClass dbClass, Connection con) {

		String sql = StatementStore.getStatement("class.delete");

		con.createQuery(sql)

		.addParameter("id", dbClass.getId())

		.executeUpdate();

	}

}
