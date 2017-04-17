package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.BookForm;
import de.sp.database.statements.StatementStore;

public class BookFormDAO {
	public static List<BookForm> getAll(Connection con) {

		String sql = StatementStore.getStatement("book_form.getAll");
		List<BookForm> book_formlist = con.createQuery(sql).executeAndFetch(
				BookForm.class);
		return book_formlist;

	}

	public static BookForm insert(Long book_id, Long form_id,
			Long curriculum_id, Integer languageyear, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("book_form.insert");

		Long id = con.createQuery(sql, true).addParameter("book_id", book_id)
				.addParameter("form_id", form_id)
				.addParameter("curriculum_id", curriculum_id)
				.addParameter("languageyear", languageyear).executeUpdate()
				.getKey(Long.class);

		return new BookForm(id, book_id, form_id, curriculum_id, languageyear);

	}

	public static void delete(Long book_form_id, Connection con) {

		String sql = StatementStore.getStatement("book_form.delete");

		con.createQuery(sql)

		.addParameter("id", book_form_id)

		.executeUpdate();

	}

	public static Long getSchoolId(Long book_form_id, Connection con) {

		String sql = StatementStore.getStatement("book_form.getSchoolId");

		return con.createQuery(sql).addParameter("book_form_id", book_form_id)
				.executeAndFetchFirst(Long.class);

	}

}
