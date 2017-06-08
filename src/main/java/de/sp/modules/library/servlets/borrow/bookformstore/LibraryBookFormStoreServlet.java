package de.sp.modules.library.servlets.borrow.bookformstore;

import com.google.gson.Gson;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class LibraryBookFormStoreServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new Gson();

		BookFormStoreRequest requestData = gson.fromJson(postData,
				BookFormStoreRequest.class);

		String responseString;

		try (Connection con = ConnectionPool.beginTransaction()) {

			requestData.validate(ts);

			user.checkPermission(LibraryModule.PERMISSION_LIBRARY,
					requestData.getSchool_id());

			List<BookFormStoreRecord> records = LibraryDAO.getBookFormStore(
					requestData.getSchool_id(), con);

			responseString = gson.toJson(records);

		} catch (Exception ex) {
			logger.error("Error serving Request", ex);
			responseString = "[]";
		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(responseString);

	}

}
