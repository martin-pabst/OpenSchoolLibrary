package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.StudentSchoolTerm;
import de.sp.database.statements.StatementStore;

public class StudentSchoolTermDAO {
	public static List<StudentSchoolTerm> getAll(Connection con) {

		String sql = StatementStore.getStatement("student_school_term.getAll");
		List<StudentSchoolTerm> student_school_termlist = con.createQuery(sql)
				.executeAndFetch(StudentSchoolTerm.class);
		return student_school_termlist;

	}

	public static StudentSchoolTerm insert(Long student_id,
			Long school_term_id, Long class_id, Long curriculum_id,
			Connection con) throws Exception {

		String sql = StatementStore.getStatement("student_school_term.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("student_id", student_id)
				.addParameter("school_term_id", school_term_id)
				.addParameter("class_id", class_id)
				.addParameter("curriculum_id", curriculum_id).executeUpdate()
				.getKey(Long.class);

		return new StudentSchoolTerm(id, student_id, school_term_id, class_id,
				curriculum_id);

	}

	public static void delete(StudentSchoolTerm student_school_term,
			Connection con) {

		String sql = StatementStore.getStatement("student_school_term.delete");

		con.createQuery(sql)

		.addParameter("id", student_school_term.getId())

		.executeUpdate();

	}

	public static List<StudentSchoolTerm> findByStudentAndSchoolTerm(
			Long student_id, Long school_term_id, Connection con)
			throws Exception {

		String sql = StatementStore
				.getStatement("student_school_term.findByStudentAndSchoolTerm");

		List<StudentSchoolTerm> sst = con.createQuery(sql)
				.addParameter("student_id", student_id)
				.addParameter("school_term_id", school_term_id)
				.executeAndFetch(StudentSchoolTerm.class);

		return sst;

	}

	public static void update(StudentSchoolTerm studentSchoolTerm,
			Connection con) {

		String sql = StatementStore.getStatement("student_school_term.update");

		con.createQuery(sql)

				.addParameter("id", studentSchoolTerm.getId())
				.addParameter("student_id", studentSchoolTerm.getStudent_id())
				.addParameter("school_term_id",
						studentSchoolTerm.getSchool_term_id())
				.addParameter("class_id", studentSchoolTerm.getClass_id())
				.addParameter("curriculum_id",
						studentSchoolTerm.getCurriculum_id()).executeUpdate();

	}

}
