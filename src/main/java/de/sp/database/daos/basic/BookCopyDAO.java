package de.sp.database.daos.basic;

import de.sp.database.model.BookCopy;
import de.sp.database.statements.StatementStore;
import de.sp.modules.library.servlets.inventory.copies.BookCopyInfoRecord;
import de.sp.modules.library.servlets.inventory.copies.LibraryInventoryCopiesRecord;
import de.sp.modules.library.servlets.tools.FindBookIdByBarcodeResponse;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.UPCEANLogicImpl;
import org.sql2o.Connection;

import java.util.Calendar;
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


	public static void changeBarcode(String oldBarcode, String newBarcode, Long school_id, Connection con){

		oldBarcode = convertBarcodeToDatabaseFormat(oldBarcode);
		newBarcode = convertBarcodeToDatabaseFormat(newBarcode);

		String sql = StatementStore.getStatement("book_copy.changeBarcode");

		con.createQuery(sql)
				.addParameter("oldBarcode", oldBarcode)
				.addParameter("newBarcode", newBarcode)
				.addParameter("school_id", school_id)
				.executeUpdate();

	}

	public static BookCopy insert(Long book_id, String edition, String barcode,
			Date purchase_date,
			Connection con) throws Exception {

		barcode = convertBarcodeToDatabaseFormat(barcode);

		if(purchase_date == null){
			purchase_date = Calendar.getInstance().getTime();
		}

		String sql = StatementStore.getStatement("book_copy.insert");

		Long id = con.createQuery(sql, true).addParameter("book_id", book_id)
				.addParameter("edition", edition)
				.addParameter("barcode", barcode)
				.addParameter("purchase_date", purchase_date)
				.executeUpdate()
				.getKey(Long.class);

		return new BookCopy(id, book_id, edition, barcode, purchase_date, null);

	}

	private static String completeBarcode(String barcode, boolean checksumAlreadyIncluded) {
		while(barcode.length() < 12 + (checksumAlreadyIncluded ? 1 : 0)){
			barcode = "0" + barcode;
		}

		if(barcode.length() < 13) {
			barcode = addCheckSumEAN13(barcode);
		}
		return barcode;
	}

	private static String convertBarcodeToDatabaseFormat(String barcode){

		while(barcode.startsWith("0")){
			barcode = barcode.substring(1);
		}

		return barcode;
	}

	public static String addLeadingZerosToGetEAN13(String barcode){
		while (barcode.length() < 13){
			barcode = "0" + barcode;
		}

		return barcode;
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

		barcode = convertBarcodeToDatabaseFormat(barcode);

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



    public static String addCheckSumEAN13(String codigo) {

		String codigo12 = codigo;
		while(codigo12.length() < 12){
			codigo12 = "0" + codigo12;
		}

        EAN13Bean generator = new EAN13Bean();
        UPCEANLogicImpl impl = generator.createLogicImpl();
        codigo += impl.calcChecksum(codigo12);
        return codigo;
    }

    public static FindBookIdByBarcodeResponse findBookIdByBarcode(String barcode, Long school_id, Connection con) {

		barcode = convertBarcodeToDatabaseFormat(barcode);

		String sql = StatementStore.getStatement("book_copy.findIdByBarcode");

		FindBookIdByBarcodeResponse response =
				con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("barcode", barcode)
				.executeAndFetchFirst(FindBookIdByBarcodeResponse.class);

		return response;
	}
}
