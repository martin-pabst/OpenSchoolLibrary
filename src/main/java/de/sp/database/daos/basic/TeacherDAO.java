package de.sp.database.daos.basic;

import de.sp.database.model.Teacher;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

public class TeacherDAO {
	public static List<Teacher> getAll(Connection con) {

		String sql = StatementStore.getStatement("teacher.getAll");
		List<Teacher> teacherlist = con.createQuery(sql).executeAndFetch(
				Teacher.class);
		return teacherlist;

	}

	public static Teacher insert(Long school_id, Long user_id, String surname,
			String firstname, String before_surname, String after_surname,
			String abbreviation, String external_id, String grade,
			boolean is_synchronized,
			Connection con) throws Exception {

		String sql = StatementStore.getStatement("teacher.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("school_id", school_id)
				.addParameter("user_id", user_id)
				.addParameter("surname", surname)
				.addParameter("firstname", firstname)
				.addParameter("before_surname", before_surname)
				.addParameter("after_surname", after_surname)
				.addParameter("abbreviation", abbreviation)
				.addParameter("external_id", external_id)
				.addParameter("grade", grade)
				.addParameter("synchronized", is_synchronized)
				.executeUpdate()
				.getKey(Long.class);

		return new Teacher(id, school_id, user_id, surname, firstname,
				before_surname, after_surname, abbreviation, external_id, grade, is_synchronized);

	}

	public static void delete(Teacher teacher, Connection con) {

		String sql = StatementStore.getStatement("teacher.delete");

		con.createQuery(sql)

		.addParameter("id", teacher.getId())

		.executeUpdate();

	}

	public static List<Teacher> findBySchool(Long school_id, Connection con) {

		String sql = StatementStore.getStatement("teacher.findBySchool");

		List<Teacher> teacherlist = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.executeAndFetch(Teacher.class);

		return teacherlist;

	}

	public static void update(Teacher t, Connection con) {
		String sql = StatementStore.getStatement("teacher.update");

		con.createQuery(sql, false).addParameter("surname", t.getSurname())
				.addParameter("firstname", t.getFirstname())
				.addParameter("abbreviation", t.getAbbreviation())
				.addParameter("external_id", t.getExternal_id())
				.addParameter("grade", t.getGrade())
				.addParameter("id", t.getId())
				.addParameter("synchronized", t.isSynchronized())
				.executeUpdate();

	}

	public static Long getSchoolId(Long teacher_id, Connection con) {

		String sql = StatementStore.getStatement("teacher.getSchoolId");

		return con.createQuery(sql).addParameter("teacher_id", teacher_id)
				.executeAndFetchFirst(Long.class);

	}

	public static void setSynchronizedForAll(Long school_id, boolean is_synchronized,
													 Connection con){
		String sql = StatementStore.getStatement("teacher.setSynchronized");

		con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("synchronized", is_synchronized)
				.executeUpdate();
	}


}
