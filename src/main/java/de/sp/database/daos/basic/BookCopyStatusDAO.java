package de.sp.database.daos.basic;

import java.util.Date;
import java.util.List;

import org.sql2o.Connection;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.BookCopyStatus;
import de.sp.database.statements.StatementStore;

public class BookCopyStatusDAO {
	public static List<BookCopyStatus> getAll() {

		try (Connection con = ConnectionPool.open()) {

			String sql = StatementStore.getStatement("book_copy_status.getAll");
			List<BookCopyStatus> book_copy_statuslist = con.createQuery(sql)
					.executeAndFetch(BookCopyStatus.class);
			return book_copy_statuslist;

		}

	}

	public static BookCopyStatus insert(Long book_copy_id, Date statusdate,
			String evidence, String username, String borrowername, String mark,
			String picture_filenames, String event, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("book_copy_status.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("book_copy_id", book_copy_id)
				.addParameter("statusdate", statusdate)
				.addParameter("evidence", evidence)
				.addParameter("username", username)
				.addParameter("borrowername", borrowername)
				.addParameter("mark", mark)
				.addParameter("picture_filenames", picture_filenames)
				.addParameter("event", event).executeUpdate()
				.getKey(Long.class);

		return new BookCopyStatus(id, book_copy_id, statusdate, evidence,
				username, borrowername, mark, picture_filenames, event);

	}

	public static void delete(Long id, Connection con) {

		String sql = StatementStore.getStatement("book_copy_status.delete");

		con.createQuery(sql)

		.addParameter("id", id)

		.executeUpdate();

	}

	public static List<BookCopyStatus> findByBarcodeAndSchool(String barcode,
			Long school_id) {

		try (Connection con = ConnectionPool.open()) {

			String sql = StatementStore
					.getStatement("book_copy_status.findByBarcodeAndSchool");
			List<BookCopyStatus> book_copy_statuslist = con.createQuery(sql)
					.addParameter("barcode", barcode)
					.addParameter("school_id", school_id)
					.executeAndFetch(BookCopyStatus.class);
			return book_copy_statuslist;

		}

	}

	public static List<BookCopyStatus> findById(Long book_copy_id,
			Connection con) {

		String sql = StatementStore.getStatement("book_copy_status.findById");
		List<BookCopyStatus> book_copy_statuslist = con.createQuery(sql)
				.addParameter("book_copy_id", book_copy_id)
				.executeAndFetch(BookCopyStatus.class);
		return book_copy_statuslist;

	}

}
