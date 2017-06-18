package de.sp.database.daos.basic;

import de.sp.database.model.School;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolDAO {


	public static List<School> getAll(Connection con) {

		String sql = StatementStore.getStatement("school.getAll");

		List<School> schoollist = con.createQuery(sql).executeAndFetch(
				School.class);

		return schoollist;

	}

	public static School findByNumber(Connection con, String schoolNumber) {

		String sql = StatementStore.getStatement("school.findByNumber");

		List<School> schoollist = con.createQuery(sql)
				.addParameter("number", schoolNumber)
				.executeAndFetch(School.class);

		if(schoollist.size() == 0){
			return null;
		}

		return schoollist.get(0);

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

    public static void remove(Long school_id, Connection con) {

		String sql = StatementStore.getStatement("school.remove");

		con.createQuery(sql)
				.addParameter("id", school_id)
				.executeUpdate();

	}

	public static void update(School school, Connection con) {

		String sql = StatementStore.getStatement("school.update");

		con.createQuery(sql)
				.addParameter("number", school.getNumber())
				.addParameter("name", school.getName())
				.addParameter("abbreviation", school.getAbbreviation())
				.executeUpdate();

	}
}
