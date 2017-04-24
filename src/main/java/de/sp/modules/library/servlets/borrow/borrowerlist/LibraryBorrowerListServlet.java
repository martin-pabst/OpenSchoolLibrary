package de.sp.modules.library.servlets.borrow.borrowerlist;

import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.protocols.w2ui.grid.gridrequest.BaseGridServlet;
import de.sp.protocols.w2ui.grid.gridrequest.GridRequestGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import org.sql2o.Connection;

import java.util.List;

public class LibraryBorrowerListServlet extends BaseGridServlet<BorrowerRecord> {

	@Override
	protected GridResponseGet<BorrowerRecord> doGet(GridRequestGet getData,
			Connection con) {

		List<BorrowerRecord> records = LibraryDAO.getBorrowerList(
				getData.getSchool_id(), getData.getSchool_term_id(), con, true);

		return new GridResponseGet<BorrowerRecord>(GridResponseStatus.success,
				records.size(), records, "", null);

	}

	@Override
	protected String getRequiredPermission(String command) {
		switch (command) {
		case "get":
			return LibraryModule.PERMISSION_LIBRARY;
		default:
			return LibraryModule.PERMISSION_LIBRARY;
		}
	}

}
