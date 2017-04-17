package de.sp.modules.library.servlets.returnbooks.returnerlist;

import java.util.List;

import org.sql2o.Connection;

import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.protocols.w2ui.grid.gridrequest.BaseGridServlet;
import de.sp.protocols.w2ui.grid.gridrequest.GridRequestGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;

public class ReturnerListServlet extends BaseGridServlet<ReturnerRecord> {

	@Override
	protected GridResponseGet<ReturnerRecord> doGet(GridRequestGet getData,
			Connection con) {

		List<ReturnerRecord> records = LibraryDAO.getReturnerList(
				getData.getSchool_id(), getData.getSchool_term_id(), con);

		return new GridResponseGet<ReturnerRecord>(GridResponseStatus.success,
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
