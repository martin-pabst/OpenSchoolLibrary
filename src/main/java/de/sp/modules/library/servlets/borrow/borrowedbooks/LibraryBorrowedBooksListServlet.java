package de.sp.modules.library.servlets.borrow.borrowedbooks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.sql2o.Connection;

import de.sp.database.daos.basic.BorrowsDAO;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.protocols.w2ui.grid.gridrequest.BaseGridServlet;
import de.sp.protocols.w2ui.grid.gridrequest.GridRequestDelete;
import de.sp.protocols.w2ui.grid.gridrequest.GridRequestGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseUpdateDelete;

public class LibraryBorrowedBooksListServlet extends
		BaseGridServlet<BorrowedBookRecord> {

	@Override
	protected GridResponseGet<BorrowedBookRecord> doGet(
			GridRequestGet getData, Connection con) {

		Long reference_id = getData.getReference_id();
		String reference_type = getData.getReference_type();

		List<BorrowedBookRecord> records = null;

		if ("teacher".equals(reference_type)) {
			records = LibraryDAO.getBorrowedBooksForTeacher(reference_id, con);
		} else {
			records = LibraryDAO.getBorrowedBooksForStudent(reference_id, con);
		}

		return new GridResponseGet<BorrowedBookRecord>(
				GridResponseStatus.success, records.size(), records, "", null);

	}

	@Override
	protected GridResponseUpdateDelete doDelete(GridRequestDelete deleteData,
			Connection con) throws Exception {

		
		for (Long id : deleteData.getSelected()) {
			
			if(!deleteData.getSchool_id().equals(BorrowsDAO.findSchoolId(id, con))){
				throw new Exception("LibraryBorrowedBooksListServlet:Borrows-Entry with id " + id + " does not belong to school with id " + deleteData.getSchool_id());
			}
			
			Date return_date = Calendar.getInstance().getTime();
			BorrowsDAO.setReturnDate(id, return_date, con);
		}

		return new GridResponseUpdateDelete(GridResponseStatus.success, "");
	}

	@Override
	protected String getRequiredPermission(String command) {

		switch (command) {
		case "get":
			return LibraryModule.PERMISSION_LIBRARY;
		case "delete":
			return LibraryModule.PERMISSION_RETURN;
		default:
			return LibraryModule.PERMISSION_LIBRARY;
		}

	}

}
