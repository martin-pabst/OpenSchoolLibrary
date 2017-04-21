package de.sp.database.daos.basic;

import de.sp.database.model.Book;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

public class BookDAO {
	public static List<Book> getAll(Connection con) {

		String sql = StatementStore.getStatement("book.getAll");
		List<Book> booklist = con.createQuery(sql).executeAndFetch(Book.class);
		return booklist;

	}

	public static Book insert(Long school_id, String title, String author,
			String isbn, String publisher, String remarks, String approval_code, String edition, Long subject_id,
			Double price, Connection con) throws Exception {

		String sql = StatementStore.getStatement("book.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("school_id", school_id)
				.addParameter("title", title).addParameter("author", author)
				.addParameter("isbn", isbn)
				.addParameter("publisher", publisher)
				.addParameter("remarks", remarks)
				.addParameter("approval_code", approval_code)
				.addParameter("edition", edition)
				.addParameter("subject_id", subject_id)
				.addParameter("price", price).executeUpdate()
				.getKey(Long.class);

		return new Book(id, school_id, title, author, isbn, publisher, remarks,
				approval_code, edition,
				subject_id, price);

	}

	public static void delete(Long book_id, Connection con) {

		String sql3 = StatementStore.getStatement("book.deleteBorrows");
		con.createQuery(sql3).addParameter("book_id", book_id).executeUpdate();

		String sql4 = StatementStore.getStatement("book.deleteBook_copy");
		con.createQuery(sql4).addParameter("book_id", book_id).executeUpdate();

		String sql1 = StatementStore.getStatement("book.deleteBook_form");
		con.createQuery(sql1).addParameter("book_id", book_id).executeUpdate();

		String sql2 = StatementStore.getStatement("book.delete");

		con.createQuery(sql2).addParameter("id", book_id).executeUpdate();

	}

	public static Long getSchoolId(Long book_id, Connection con) {

		String sql = StatementStore.getStatement("book.getSchoolId");

		return con.createQuery(sql).addParameter("book_id", book_id)
				.executeAndFetchFirst(Long.class);

	}

}
