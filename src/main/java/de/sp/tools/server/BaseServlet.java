package de.sp.tools.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sp.database.model.User;
import de.sp.main.login.LoginServlet;
import de.sp.main.mainframe.MainFrameServlet;
import de.sp.main.resources.text.TS;

public class BaseServlet extends HttpServlet {

	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		Logger logger = LoggerFactory.getLogger(MainFrameServlet.class);

		HttpSession session = req.getSession();

		User user = (User) session.getAttribute(LoginServlet.USER);
		TS ts = (TS) session.getAttribute(LoginServlet.TRANSLATION_SERVICE);

		doPostExtended(req, response, logger, session, user, ts,
				getPostData(req));

	}

	protected String getPostData(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {

			BufferedReader reader = request.getReader();

			while ((line = reader.readLine()) != null)
				jb.append(line);

			return jb.toString();

		} catch (Exception e) { /* report an error */
		}

		return "";

	}

	protected String getLastURLPart(HttpServletRequest request) {

		String url = request.getRequestURL().toString();

		int j = url.lastIndexOf('#');
		
		int i = -1;
		
		if(j > -1){
			i = url.lastIndexOf('/', j);
			if(i > -1){
				return url.substring(i+1, j);
			}
		} else {
			i = url.lastIndexOf('/');
			if (i > -1) {
				return url.substring(i + 1);
			}
		}
		return "";
	}

	protected String saveCastToString(Object o) {
		return o == null ? null : o.toString();
	}

	protected Integer saveCastToInteger(Object o) {

		try {
			return new Integer((int) Double.parseDouble(o.toString()));
		} catch (Exception ex) {
			return null;
		}

	}

	protected Long saveCastToLong(Object o) {

		try {
			return new Long((long) Double.parseDouble(o.toString()));
		} catch (Exception ex) {
			return null;
		}

	}

}
