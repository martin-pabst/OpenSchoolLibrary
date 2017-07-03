package de.sp.protocols.w2ui.grid.gridrequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.server.ErrorResponse;
import de.sp.tools.validation.Validator;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseGridServlet<E> extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

		String responseString = "";

		String command = getLastURLPart(request);

		try (Connection con = ConnectionPool.beginTransaction()) {

			try {

				switch (command) {

				case "get":

					GridRequestGet getData = gson.fromJson(postData,
							GridRequestGet.class);

					Validator.validate(getData, ts);

					user.checkPermission(getRequiredPermission("get"),
							getData.getSchool_id());

					GridResponseGet<E> responseGet = doGet(getData, con);
					responseString = gson.toJson(responseGet);

					break;

				case "update":

					GridRequestUpdate updateData = gson.fromJson(postData,
							GridRequestUpdate.class);

					Validator.validate(updateData, ts);

					user.checkPermission(getRequiredPermission("update"),
							updateData.getSchool_id());
					
					GridResponseUpdateDelete responseUpdate = doUpdate(
							updateData, con);
					responseString = gson.toJson(responseUpdate);

					break;

				case "delete":

					GridRequestDelete deleteData = gson.fromJson(postData,
							GridRequestDelete.class);

					Validator.validate(deleteData, ts);

					user.checkPermission(getRequiredPermission("delete"),
							deleteData.getSchool_id());
					
					GridResponseUpdateDelete responseDelete = doDelete(
							deleteData, con);
					responseString = gson.toJson(responseDelete);

					break;

				case "save":

					GridRequestSave saveData = gson.fromJson(postData,
							GridRequestSave.class);

					Validator.validate(saveData, ts);

					user.checkPermission(getRequiredPermission("save"),
							saveData.getSchool_id());
					
					GridResponseSave responseSave = doSave(saveData, con);
					responseString = gson.toJson(responseSave);

					break;

				}

				con.commit(true);

			} catch (Exception ex) {
				logger.error("Error serving Request", ex);
				con.rollback();
				ErrorResponse resp = new ErrorResponse(ex.toString());
				responseString = gson.toJson(resp);
			}

		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(responseString);

	}

	protected GridResponseSave doSave(GridRequestSave saveData, Connection con)
			throws Exception {

		return new GridResponseSave(GridResponseStatus.error,
				"Function Save not implemented.", null);

	}

	protected GridResponseUpdateDelete doDelete(GridRequestDelete deleteData,
			Connection con) throws Exception {
		return new GridResponseUpdateDelete(GridResponseStatus.error,
				"Function delete not implemented.");
	}

	protected GridResponseGet<E> doGet(GridRequestGet getData, Connection con)
			throws Exception {

		return new GridResponseGet<E>(GridResponseStatus.error,
				"Function get is not implemented.");

	}

	protected GridResponseUpdateDelete doUpdate(GridRequestUpdate updateData,
			Connection con) throws Exception {
		return new GridResponseUpdateDelete(GridResponseStatus.error,
				"Function update not implemented.");
	}

	abstract protected String getRequiredPermission(String command);
}
