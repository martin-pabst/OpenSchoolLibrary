package de.sp.database.daos.basic;

import de.sp.database.model.Student;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDAO {

	public static List<Student> getAll(Connection con) {

		String sql = StatementStore.getStatement("student.getAll");
		List<Student> studentlist = con.createQuery(sql).executeAndFetch(
				Student.class);
		return studentlist;

	}

	public static List<Student> findBySchoolId(Long school_id, Connection con) {

		String sql = StatementStore.getStatement("student.findBySchoolID");
		List<Student> studentlist = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.executeAndFetch(Student.class);
		return studentlist;

	}

	public static Student insert(Long school_id, Date dateofbirth,
			String surname, String firstname, String christian_names,
			String before_surname, String after_surname, Integer sex_key,
			String external_id, Date exit_date, boolean is_synchronized,
								 Long religion_id, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("student.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("school_id", school_id)
				.addParameter("dateofbirth", dateofbirth)
				.addParameter("surname", surname)
				.addParameter("firstname", firstname)
				.addParameter("christian_names", christian_names)
				.addParameter("before_surname", before_surname)
				.addParameter("after_surname", after_surname)
				.addParameter("sex_key", sex_key)
				.addParameter("external_id", external_id)
				.addParameter("exit_date", exit_date)
				.addParameter("synchronized", is_synchronized ? 1 : 0)
				.addParameter("religion_id", religion_id)
				.executeUpdate()
				.getKey(Long.class);

		return new Student(id, school_id, dateofbirth, surname, firstname,
				christian_names, before_surname, after_surname, sex_key,
				external_id, exit_date, is_synchronized, religion_id);

	}

	public static void delete(Student student, Connection con) {

		String sql = StatementStore.getStatement("student.delete");

		con.createQuery(sql)

		.addParameter("id", student.getId())

		.executeUpdate();

	}

	public static void deleteCascadingByStudentList(List<Student> students, Connection con){

		List<Long> ids = new ArrayList<>();

		students.forEach(student -> ids.add(student.getId()));

		deleteCascading(ids, con);
	}

	public static void deleteCascading(List<Long> student_ids, Connection con){
		String ids =
				student_ids.stream().map(id -> id.toString()).collect(Collectors.joining(", "));

		for (String sql : StatementStore.getStatements("student.deleteCascading")) {

			sql = sql.replace(":ids", ids);

			con.createQuery(sql).executeUpdate();

		}

	}

	public static void update(Student student, Connection con) {

		String sql = StatementStore.getStatement("student.update");

		con.createQuery(sql)

		.addParameter("id", student.getId())
				.addParameter("school_id", student.getSchool_id())
				.addParameter("dateofbirth", student.getDateofbirth())
				.addParameter("surname", student.getSurname())
				.addParameter("firstname", student.getFirstname())
				.addParameter("christian_names", student.getChristian_names())
				.addParameter("before_surname", student.getBefore_surname())
				.addParameter("after_surname", student.getAfter_surname())
				.addParameter("sex_key", student.getSex_key())
				.addParameter("external_id", student.getExternal_id())
				.addParameter("exit_date", student.getExit_date())
				.addParameter("synchronized", student.isSynchronized() ? 1 : 0)
				.addParameter("religion_id", student.getReligion_id())
				.executeUpdate();


	}

	public static Long getSchoolId(Long student_id, Connection con) {

		String sql = StatementStore.getStatement("student.getSchoolId");

		return con.createQuery(sql).addParameter("student_id", student_id)
				.executeAndFetchFirst(Long.class);

	}

	public static void setAddressId(Long student_id, Long address_id,
			Connection con) {
		
		String sql = StatementStore.getStatement("student.setAddressId");

		con.createQuery(sql).addParameter("student_id", student_id)
				.addParameter("address_id", address_id)
				.executeUpdate();

		
	}

    public static void updateBorrower(Long student_id, Date date_of_birth,
									  String surname, String firstname, String before_surname,
									  String after_surname, Long sex_id,
									  Long religion_id, Connection con) {

		String sql = StatementStore.getStatement("student.updateBorrower");

		con.createQuery(sql)

				.addParameter("id", student_id)
				.addParameter("dateofbirth", date_of_birth)
				.addParameter("surname", surname)
				.addParameter("firstname", firstname)
				.addParameter("before_surname", before_surname)
				.addParameter("after_surname", after_surname)
				.addParameter("sex_key", sex_id)
				.addParameter("religion_id", religion_id)
				.executeUpdate();
	}

	public static void setSynchronizedForAll(Long school_id, boolean is_synchronized,
													 Connection con){
		String sql = StatementStore.getStatement("student.setSynchronized");

		con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("synchronized", is_synchronized ? 1 : 0)
				.executeUpdate();
	}

}
