package de.sp.modules.library.servlets.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.inventory.books.LibraryInventoryDAO;
import de.sp.modules.library.servlets.reports.model.ReportManager;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseSave;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LibraryNavigatorDataServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

		String responseString = "[]";

		NavigatorDataRequest navigatorDataRequest = gson.fromJson(postData, NavigatorDataRequest.class);



		try (Connection con = ConnectionPool.beginTransaction()) {

			user.checkPermission(LibraryModule.PERMISSION_REPORTS,
					navigatorDataRequest.school_id);

			responseString =
					getNavigatorData(con, navigatorDataRequest.school_id, navigatorDataRequest.school_term_id);

			con.commit();

		} catch (Exception ex) {
			logger.error(this.getClass().toString() + ": Error serving data",
					ex);
			responseString = gson.toJson(new GridResponseSave(
					GridResponseStatus.error, ex.toString(), null));
		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(responseString);

	}

	private String getNavigatorData(Connection con, Long school_id, Long school_term_id) {

		NavigatorDataResponse navigatorDataResponse = new NavigatorDataResponse();

		navigatorDataResponse.schueler = LibraryDAO.getBorrowerList(
				school_id, school_term_id, con);

		navigatorDataResponse.extractLehrerFromSchuelerList();

		navigatorDataResponse.buecher = LibraryInventoryDAO
				.getInventoryList(school_id, 1000000,
						0, con);

		navigatorDataResponse.klassen = LibraryDAO.getClassList(school_term_id, con);

		Gson gson = new Gson();
		String json = gson.toJson(navigatorDataResponse);
		json = json.replace("\"xx\"", ReportManager.getInstance().toJSon());

		System.out.println(json);

		return json;

	}


}
