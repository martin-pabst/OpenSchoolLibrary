package de.sp.modules.library.servlets.borrow.bookcopystatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.daos.basic.BookCopyStatusDAO;
import de.sp.database.model.BookCopyStatus;
import de.sp.database.model.User;
import de.sp.main.resources.modules.InsufficientPermissionException;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.ReturnBookResponse;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseSave;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.validation.ValidationException;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class LibraryBookCopyStatusServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

		String responseString = "[]";

		try (Connection con = ConnectionPool.beginTransaction()) {

			String command = getLastURLPart(request);

			switch (command) {
			case "get":
				responseString = doGet(user, con, postData, gson, ts);
				break;
			case "save":
				responseString = doSave(user, con, postData, gson, ts);
				break;
			}

			con.commit();

		} catch (Exception ex) {
			logger.error("LibraryBookCopyStatusServlet: Error serving Request",
					ex);
			responseString = gson.toJson(new GridResponseSave(
					GridResponseStatus.error, ex.toString(), null));
		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(responseString);

	}

	private String doSave(User user, Connection con, String postData, Gson gson, TS ts)
			throws Exception {

		SaveBookCopyStatusRequest request = gson.fromJson(postData,
				SaveBookCopyStatusRequest.class);

		request.validate(ts);

		user.checkPermission(LibraryModule.PERMISSION_EXAMINE,
				request.getSchool_id());

		if (!request.getSchool_id().equals(
				BookCopyDAO.getSchoolIdForBookCopy(request.getBook_copy_id(),
						con))) {
			throw new Exception("BookCopy with id " + request.getBook_copy_id()
					+ " belongs not to school with id "
					+ request.getSchool_id());
		}

		@SuppressWarnings("unused")
		BookCopyStatus status = BookCopyStatusDAO.insert(
				request.getBook_copy_id(), request.getStatusDate(),
				request.getEvidence(), request.getUsername(),
				request.getBorrowername(), request.getMark(), "",
				request.getEvent(), con);

		return gson.toJson(new GridResponseSave(GridResponseStatus.success, "",
				null));
	}

	private String doGet(User user, Connection con, String postData, Gson gson, TS ts)
			throws InsufficientPermissionException, ValidationException {

		GetBookCopyStatusRequest request = gson.fromJson(postData,
				GetBookCopyStatusRequest.class);

		request.validate(ts);

		user.checkPermission(LibraryModule.PERMISSION_OPEN,
				request.getSchool_id());

		List<BookCopyStatus> statusList = BookCopyStatusDAO
				.findByBarcodeAndSchool(request.getBarcode(),
						request.getSchool_id());

		ReturnBookResponse returnBookResponse = LibraryDAO.getReturnBookResponse(request.getBarcode(),
				request.getSchool_id(), con);

		BookCopyStatusResponse resp = new BookCopyStatusResponse(statusList, returnBookResponse);

		return gson.toJson(resp);
	}

}
