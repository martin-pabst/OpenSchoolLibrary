package de.sp.modules.install.servlets;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.sql2o.Connection;

import com.google.gson.Gson;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.model.User;
import de.sp.database.setup.DatabaseSetup;
import de.sp.database.setup.DatabaseStateEnum;
import de.sp.database.setup.DatabaseTester;
import de.sp.main.config.Configuration;
import de.sp.main.login.LoginServlet;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.server.progressServlet.ProgressServlet;

public class InitDBServlet extends BaseServlet {

	private Template template;

	@Override
	public void init(ServletConfig config) throws ServletException {

		template = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/install/install.vm");

		super.init(config);
	}

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		DatabaseStateEnum databaseState = DatabaseTester.test();

		if (databaseState == DatabaseStateEnum.databaseNotEmpty) {
			response.sendRedirect("/login");
		}

		try {
			
			Configuration config = Configuration.getInstance();

			Gson gson = new Gson();
			
			InitDBRequest requestData = gson.fromJson(postData, InitDBRequest.class);

			final String progressCode = ProgressServlet.publishProgress(0, 100, 0, "", false, "", null);
			
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {

					try {
					
					DatabaseSetup dbSetup = new DatabaseSetup();

					dbSetup.setup(true, progressCode);

					try (Connection con = ConnectionPool.open()) {
					
						User user1 = UserDAO.insert(requestData.username, requestData.name, requestData.password,
								config.getLanguagesConfig().defaultCode, null, true, null, con);
						
						session.setAttribute(LoginServlet.USER, user1);

					} 

					ProgressServlet.publishProgress(0, 100, 100, "", true, "success", progressCode);
					
					} catch(Exception ex){
						ProgressServlet.publishProgress(0, 100, 100, "Fehler!", true, ex.toString(), progressCode);
						logger.error(ex.toString(), ex);
					}

					
				}
			});
			
			t.start();
			
			
			response.setContentType("text/json");
			response.setStatus(HttpServletResponse.SC_OK);

			response.getWriter().println(gson.toJson(new InitDBResponse(progressCode)));

		} catch (Exception e) {

			logger.error(e.toString(), e);

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		DatabaseStateEnum databaseState = DatabaseTester.test();

		if (databaseState == DatabaseStateEnum.databaseNotEmpty) {
			response.sendRedirect("/login");
		}

		Configuration config = Configuration.getInstance();

		TS ts = new TS(config.getLanguagesConfig().defaultCode);
		HttpSession session = req.getSession();
		
		session.setAttribute(LoginServlet.TRANSLATION_SERVICE, ts);

		/* create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put("noDatabase",
				databaseState == DatabaseStateEnum.connectionError);
		context.put("TS", ts);

		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(writer.toString());

	}
}
