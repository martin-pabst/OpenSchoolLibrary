package de.sp.modules.library.servlets.inventory.copies;

import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.daos.basic.BookDAO;
import de.sp.database.model.BookCopy;
import de.sp.modules.library.LibraryModule;
import de.sp.protocols.w2ui.grid.gridrequest.*;
import org.sql2o.Connection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LibraryInventoryCopiesServlet extends
		BaseGridServlet<LibraryInventoryCopiesRecord> {

	@Override
	protected GridResponseUpdateDelete doDelete(GridRequestDelete deleteData,
			Connection con) throws Exception {

		for (Long id : deleteData.getSelected()) {

			if (!deleteData.getSchool_id().equals(
					BookCopyDAO.getSchoolIdForBookCopy(id, con))) {
				throw new Exception("BookCopy with id " + id
						+ " does not belong to school with id "
						+ deleteData.getSchool_id());
			}

			BookCopyDAO.setSortedOutDate(id, Calendar.getInstance().getTime(), con);

		}

		return new GridResponseUpdateDelete(GridResponseStatus.success, "");
	}

	@Override
	protected GridResponseSave doSave(GridRequestSave saveData, Connection con)
			throws Exception {

		Map<String, Object> record = saveData.getRecord();

		Object book_id_obj = record.get("book_id");

		Long book_id = saveCastToLong(book_id_obj);

		if (!saveData.getSchool_id().equals(BookDAO.getSchoolId(book_id, con))) {
			throw new Exception("Book with id " + book_id
					+ " does not belong to school with id "
					+ saveData.getSchool_id());
		}

		String barcode = record.get("barcode").toString();

		String edition = saveCastToString(record.get("edition"));

		Date purchase_date = null;
		try {
			String dateString = saveCastToString(record.get("purchase_date"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			purchase_date = sdf.parse(dateString);
		} catch(Exception ex){
			purchase_date = Calendar.getInstance().getTime();
		}

		List<BookCopyInfoRecord> bookCopyInfoList = BookCopyDAO
				.getBookCopyInInfo(saveData.getSchool_id(), barcode, con);

		if (bookCopyInfoList.size() > 0) {

			BookCopyInfoRecord bci = bookCopyInfoList.get(0);

			String message = "Der Barcode wurde beim Buch '" + bci.getTitle()
					+ "' (" + bci.getAuthor() + ") schon registriert.";

			return new GridResponseSave(GridResponseStatus.error, message, null);

		} else {

			BookCopy bookCopy = BookCopyDAO.insert(book_id, edition, barcode, purchase_date,
					con);

			return new GridResponseSave(GridResponseStatus.success, "",
					bookCopy.getId());

		}

	}

	@Override
	protected GridResponseGet<LibraryInventoryCopiesRecord> doGet(
			GridRequestGet getData, Connection con) throws Exception {

		Long book_id = getData.getReference_id();

		if (!getData.getSchool_id().equals(BookDAO.getSchoolId(book_id, con))) {
			throw new Exception("Book with id " + book_id
					+ " does not belong to school with id "
					+ getData.getSchool_id());
		}

		List<LibraryInventoryCopiesRecord> records = BookCopyDAO
				.getBookCopyInventory(book_id, getData.getSchool_term_id(), con);

		return new GridResponseGet<LibraryInventoryCopiesRecord>(
				GridResponseStatus.success, records.size(), records, "", null);

	}


	@Override
	protected String getRequiredPermission(String command) {
		switch (command) {
		case "get":
			return LibraryModule.PERMISSION_INVENTORY_READ;
		case "update":
			return LibraryModule.PERMISSION_INVENTORY_WRITE_COPIES;
		case "save":
			return LibraryModule.PERMISSION_INVENTORY_WRITE_COPIES;
		case "delete":
			return LibraryModule.PERMISSION_INVENTORY_WRITE_COPIES;

		default:
			return LibraryModule.PERMISSION_OPEN;
		}
	}
}
