package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.Contact;
import de.sp.database.statements.StatementStore;

public class ContactDAO {
	public static List<Contact> getAll(Connection con) {

		String sql = StatementStore.getStatement("contact.getAll");
		List<Contact> contactlist = con.createQuery(sql).executeAndFetch(
				Contact.class);
		return contactlist;

	}

	public static Contact insert(String text, String name, String remark,
			Integer order_number, Long contact_type_id, Long person_id,
			Long student_id, Long teacher_id, Connection con) throws Exception {

		String sql = StatementStore.getStatement("contact.insert");

		Long id = con.createQuery(sql, true).addParameter("text", text)
				.addParameter("name", name).addParameter("remark", remark)
				.addParameter("order_number", order_number)
				.addParameter("contact_type_id", contact_type_id)
				.addParameter("person_id", person_id)
				.addParameter("student_id", student_id)
				.addParameter("teacher_id", teacher_id).executeUpdate()
				.getKey(Long.class);

		return new Contact(id, text, name, remark, order_number,
				contact_type_id, person_id, student_id, teacher_id);

	}

	public static void update(Contact contact, Connection con) throws Exception {

		String sql = StatementStore.getStatement("contact.update");

		con.createQuery(sql).addParameter("id", contact.getId())
				.addParameter("text", contact.getText())
				.addParameter("name", contact.getName())
				.addParameter("remark", contact.getRemark())
				.addParameter("order_number", contact.getOrder_number())
				.addParameter("contact_type_id", contact.getContact_type_id())
				.addParameter("person_id", contact.getPerson_id())
				.addParameter("student_id", contact.getStudent_id())
				.addParameter("teacher_id", contact.getTeacher_id())
				.executeUpdate();
	}

	public static void delete(Long id, Connection con) {

		String sql = StatementStore.getStatement("contact.delete");

		con.createQuery(sql)

		.addParameter("id", id)

		.executeUpdate();

	}

	public static Contact findById(Long id, Connection con) {

		String sql = StatementStore.getStatement("contact.findById");
		Contact contact = con.createQuery(sql).executeAndFetchFirst(
				Contact.class);
		return contact;

	}

	public static void deleteByPersonId(Long person_id, Connection con) {

		String sql = StatementStore.getStatement("contact.deleteByPersonId");

		con.createQuery(sql)

		.addParameter("person_id", person_id)

		.executeUpdate();

	}

	public static void deleteByStudentId(Long student_id, Connection con) {

		String sql = StatementStore.getStatement("contact.deleteByStudentId");

		con.createQuery(sql)

		.addParameter("student_id", student_id)

		.executeUpdate();

	}

}
