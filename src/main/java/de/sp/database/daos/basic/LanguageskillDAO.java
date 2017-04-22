package de.sp.database.daos.basic;

import de.sp.database.model.Languageskill;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

public class LanguageskillDAO {
	public static List<Languageskill> getAll(Connection con) {

		String sql = StatementStore.getStatement("languageskill.getAll");
		List<Languageskill> languageskilllist = con.createQuery(sql)
				.executeAndFetch(Languageskill.class);
		return languageskilllist;

	}

	public static Languageskill insert(Long student_id, Long subject_id,
			Integer from_year, Integer to_year, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("languageskill.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("student_id", student_id)
				.addParameter("subject_id", subject_id)
				.addParameter("from_year", from_year)
				.addParameter("to_year", to_year).executeUpdate()
				.getKey(Long.class);

		return new Languageskill(id, student_id, subject_id, from_year, to_year);

	}

	public static void delete(Languageskill languageskill, Connection con) {

		String sql = StatementStore.getStatement("languageskill.delete");

		con.createQuery(sql)

		.addParameter("id", languageskill.getId())

		.executeUpdate();

	}

	public static List<Languageskill> findByStudentID(Long student_id,
			Connection con) {

		String sql = StatementStore
				.getStatement("languageskill.findByStudentID");
		List<Languageskill> languageskilllist = con.createQuery(sql)
				.addParameter("student_id", student_id)
				.executeAndFetch(Languageskill.class);
		return languageskilllist;

	}

    public static void deleteByStudentId(Long student_id, Connection con) {

		String sql = StatementStore.getStatement("languageskill.deleteByStudentID");

		con.createQuery(sql)

				.addParameter("student_id", student_id)

				.executeUpdate();


	}
}
