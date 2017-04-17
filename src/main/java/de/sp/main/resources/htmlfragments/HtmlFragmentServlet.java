package de.sp.main.resources.htmlfragments;

import com.google.gson.Gson;
import de.sp.database.model.User;
import de.sp.main.resources.modules.ModuleManager;
import de.sp.main.resources.text.TS;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HtmlFragmentServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		String fragment = "";

		try {
			Gson gson = new Gson();

			HtmlFragmentRequest requestData = gson.fromJson(postData,
					HtmlFragmentRequest.class);

			fragment = ModuleManager.getHtmlFragment(
					requestData.getFragmentId(), ts, user,
					requestData.getSchool_id());

		} catch (Exception ex) {
			fragment = "Error serving Html fragment: " + ex.toString();
			logger.error(fragment);
		}

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(fragment);

	}

}
