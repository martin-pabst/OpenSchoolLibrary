package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.Subject;
import de.sp.database.statements.StatementStore;

public class SubjectDAO {

	public static List<Subject> getAll(Connection con) {

		String sql = StatementStore.getStatement("subject.getAll");

		List<Subject> subjectlist = con.createQuery(sql).executeAndFetch(
				Subject.class);

		return subjectlist;

	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static Subject insert(String shortname, String longname,
			Long school_id, String key1, String key2, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("subject.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("shortname", shortname)
				.addParameter("longname", longname)
				.addParameter("school_id", school_id)
				.addParameter("key1", key1).addParameter("key2", key2)
				.executeUpdate().getKey(Long.class);

		return new Subject(id, shortname, longname, school_id, key1, key2);

	}

	public static void delete(Subject subject, Connection con) {

		String sql = StatementStore.getStatement("subject.delete");

		con.createQuery(sql).addParameter("id", subject.getId())
				.executeUpdate();

	}

	public static List<Subject> findBySchoolIDAndKey1(Long school_id,
			String key1, Connection con) {

		String sql = StatementStore
				.getStatement("subject.findBySchoolIDAndKey1");

		List<Subject> subjectlist = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("key1", key1).executeAndFetch(Subject.class);

		return subjectlist;

	}

	public static Subject findFirstBySchoolIDAndKey1(Long school_id,
			String key1, Connection con) {

		List<Subject> subjects = findBySchoolIDAndKey1(school_id, key1, con);

		if (subjects != null && subjects.size() > 0) {
			return subjects.get(0);
		}

		return null;
	}

}
