package de.sp.modules.library.servlets.inventory.copies;

import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.daos.basic.BookDAO;
import de.sp.database.model.BookCopy;
import de.sp.modules.library.LibraryModule;
import de.sp.protocols.w2ui.grid.gridrequest.*;
import org.sql2o.Connection;

import java.util.Calendar;
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


/*
			 Wenn noch eine Gebühr für das Buch offen ist, kann noch nicht endgültig gelöscht werden
			 TODO: Bereinigungsfunktion einbauen, die alle Bücher findet, die wegen einer offenen Gebühr noch nicht
			 gelöscht wurden aber jetzt gelöscht werden können.
*/

			List<Long> feeIds = BookCopyDAO.getFeeIds(id, con);

			if(feeIds.size() > 0){
				BookCopyDAO.setSortedOutDate(id, Calendar.getInstance().getTime(), con);
			} else {
				BookCopyDAO.delete(id, con);
			}


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

		List<BookCopyInfoRecord> bookCopyInfoList = BookCopyDAO
				.getBookCopyInInfo(saveData.getSchool_id(), barcode, con);

		if (bookCopyInfoList.size() > 0) {

			BookCopyInfoRecord bci = bookCopyInfoList.get(0);

			String message = "Der Barcode wurde beim Buch '" + bci.getTitle()
					+ "' (" + bci.getAuthor() + ") schon registriert.";

			return new GridResponseSave(GridResponseStatus.error, message, null);

		} else {

			BookCopy bookCopy = BookCopyDAO.insert(book_id, edition, barcode,
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
			return LibraryModule.PERMISSION_LIBRARY;
		case "update":
			return LibraryModule.PERMISSION_INVENTORY;
		case "save":
			return LibraryModule.PERMISSION_INVENTORY;
		case "delete":
			return LibraryModule.PERMISSION_INVENTORY;

		default:
			return LibraryModule.PERMISSION_LIBRARY;
		}
	}
}
