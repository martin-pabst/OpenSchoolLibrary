package de.sp.database.daos.basic;

import de.sp.database.model.BookCopy;
import de.sp.database.statements.StatementStore;
import de.sp.modules.library.servlets.inventory.copies.BookCopyInfoRecord;
import de.sp.modules.library.servlets.inventory.copies.LibraryInventoryCopiesRecord;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

public class BookCopyDAO {

	public static List<BookCopy> getAll(Connection con) {

		String sql = StatementStore.getStatement("book_copy.getAll");
		List<BookCopy> book_copylist = con.createQuery(sql).executeAndFetch(
				BookCopy.class);
		return book_copylist;

	}

	public static List<BookCopy> findAvailableCopiesBySchool(Connection con, Long school_id) {

		String sql = StatementStore.getStatement("book_copy.findAvailableCopiesBySchool");

		return  con.createQuery(sql)
				.addParameter("school_id", school_id)
				.executeAndFetch(BookCopy.class);

	}



	public static BookCopy insert(Long book_id, String edition, String barcode,
			Connection con) throws Exception {

		String sql = StatementStore.getStatement("book_copy.insert");

		Long id = con.createQuery(sql, true).addParameter("book_id", book_id)
				.addParameter("edition", edition)
				.addParameter("barcode", barcode).executeUpdate()
				.getKey(Long.class);

		return new BookCopy(id, book_id, edition, barcode, null);

	}

	public static void setSortedOutDate(Long book_copy_id, Date sorted_out_date, Connection con){

		String sql0 = StatementStore.getStatement("book_copy.setSorted_out_date");

		con.createQuery(sql0).addParameter("book_copy_id", book_copy_id)
				.addParameter("sorted_out_date", sorted_out_date)
				.executeUpdate();

	}

	public static void delete(Long book_copy_id, Connection con) {

		String sql0 = StatementStore.getStatement("book_copy.deleteFees");

		con.createQuery(sql0).addParameter("book_copy_id", book_copy_id)
				.executeUpdate();


		String sql1 = StatementStore.getStatement("book_copy.deleteBorrows");

		con.createQuery(sql1).addParameter("book_copy_id", book_copy_id)
				.executeUpdate();

		String sql2 = StatementStore.getStatement("book_copy.delete");

		con.createQuery(sql2)

		.addParameter("id", book_copy_id)

		.executeUpdate();

	}

	public static List<Long> getFeeIds(Long book_copy_id, Connection con){
		String sql = StatementStore.getStatement("book_copy.getFeeList");

		List<Long> feeIds = con.createQuery(sql)
				.addParameter("book_copy_id", book_copy_id)
				.executeAndFetch(Long.class);

		return feeIds;
	}

	public static List<LibraryInventoryCopiesRecord> getBookCopyInventory(
			Long book_id, Long school_term_id, Connection con) {

		String sql = StatementStore.getStatement("book_copy.inventoryList");

		List<LibraryInventoryCopiesRecord> inventoryList = con.createQuery(sql)
				.addParameter("book_id", book_id)
				.addParameter("school_term_id", school_term_id)
				.executeAndFetch(LibraryInventoryCopiesRecord.class);

		for (LibraryInventoryCopiesRecord licr : inventoryList) {

			licr.init();

		}

		return inventoryList;

	}

	public static List<BookCopyInfoRecord> getBookCopyInInfo(Long school_id,
			String barcode, Connection con) {

		String sql = StatementStore
				.getStatement("book_copy.findByBarcodeAndSchool");

		List<BookCopyInfoRecord> inventoryList = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("barcode", barcode)
				.executeAndFetch(BookCopyInfoRecord.class);

		return inventoryList;

	}

	public static Long getSchoolIdForBookCopy(Long book_copy_id, Connection con) {

		String sql = StatementStore
				.getStatement("book_copy.getSchoolIdForBookCopy");

		return con.createQuery(sql).addParameter("book_copy_id", book_copy_id)
				.executeAndFetchFirst(Long.class);
	}

}
