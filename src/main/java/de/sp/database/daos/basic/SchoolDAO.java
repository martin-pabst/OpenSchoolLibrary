package de.sp.database.daos.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;

import de.sp.database.model.School;
import de.sp.database.statements.StatementStore;

public class SchoolDAO {

	public static List<School> getAll(Connection con) {

		String sql = StatementStore.getStatement("school.getAll");

		List<School> schoollist = con.createQuery(sql).executeAndFetch(
				School.class);

		return schoollist;

	}

	public static Map<Long, School> schoolListToKeyMap(List<School> schools) {

		Map<Long, School> keyMap = new HashMap<>();

		for (School school : schools) {
			keyMap.put(school.getId(), school);
		}

		return keyMap;

	}

	/**
	 * 
	 * insert into school (name, number)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static School insert(String name, String number,
			String abbreviation, Connection con) throws Exception {

		String sql = StatementStore.getStatement("school.insert");

		Long id = con.createQuery(sql, true).addParameter("name", name)
				.addParameter("number", number)
				.addParameter("abbreviation", abbreviation).executeUpdate()
				.getKey(Long.class);

		return new School(id, name, number, abbreviation);

	}

}
