package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.Person;
import de.sp.database.statements.StatementStore;

public class PersonDAO {

	public static List<Person> getAll(Connection con) {

		String sql = StatementStore.getStatement("person.getAll");
		List<Person> personlist = con.createQuery(sql).executeAndFetch(
				Person.class);
		return personlist;

	}

	public static Person insert(String surname, String firstname,
			String before_surname, String after_surname, String grade,
			Long student_id, Long person_type_id, Long address_id,
			Boolean is_legal_parent, Boolean is_primary_legal_parent, Connection con) throws Exception {

		String sql = StatementStore.getStatement("person.insert");

		Long id = con.createQuery(sql, true).addParameter("surname", surname)
				.addParameter("firstname", firstname)
				.addParameter("before_surname", before_surname)
				.addParameter("after_surname", after_surname)
				.addParameter("grade", grade)
				.addParameter("student_id", student_id)
				.addParameter("person_type_id", person_type_id)
				.addParameter("address_id", address_id)
				.addParameter("is_legal_parent", is_legal_parent)
				.addParameter("is_primary_legal_parent", is_primary_legal_parent)
				.executeUpdate().getKey(Long.class);

		return new Person(id, surname, firstname, before_surname,
				after_surname, grade, student_id, person_type_id, address_id,
				is_legal_parent, is_primary_legal_parent);

	}

	public static void update(Person person, Connection con) throws Exception {

		String sql = StatementStore.getStatement("person.update");

		con.createQuery(sql).addParameter("id", person.getId())
				.addParameter("surname", person.getSurname())
				.addParameter("firstname", person.getFirstname())
				.addParameter("before_surname", person.getBefore_surname())
				.addParameter("after_surname", person.getAfter_surname())
				.addParameter("grade", person.getGrade())
				.addParameter("student_id", person.getStudent_id())
				.addParameter("person_type_id", person.getPerson_type_id())
				.addParameter("address_id", person.getAddress_id())
				.addParameter("is_legal_parent", person.getIs_legal_parent())
				.addParameter("is_primary_legal_parent", person.getIs_primary_legal_parent())
				.executeUpdate();
	}

	public static void delete(Long id, Connection con) {

		String sql = StatementStore.getStatement("person.delete");

		con.createQuery(sql)

		.addParameter("id", id)

		.executeUpdate();

	}

	public static Person findById(Long id, Connection con) {

		String sql = StatementStore.getStatement("person.findById");
		Person person = con.createQuery(sql).executeAndFetchFirst(Person.class);
		return person;

	}

	public static List<Person> findPersonsByStudentId(Long student_id,
			Connection con) {

		String sql = StatementStore.getStatement("person.findByStudentId");

		List<Person> personlist = con.createQuery(sql)
				.addParameter("student_id", student_id)
				.executeAndFetch(Person.class);

		return personlist;

	}

	public static void deletePersonsForStudent(Long student_id, Connection con) {

		List<Person> persons = findPersonsByStudentId(student_id, con);
		
		for(Person person: persons){
			
			deletePerson(person, con);
						
		}
			
		StudentDAO.setAddressId(student_id, null, con);
		
		for(Person person: persons){
			AddressDAO.delete(person.getAddress_id(), con);
		}

	}

	private static void deletePerson(Person person, Connection con) {

		ContactDAO.deleteByPersonId(person.getId(), con);
	
		delete(person.getId(), con);
		
		
		

	}

	
	
}
