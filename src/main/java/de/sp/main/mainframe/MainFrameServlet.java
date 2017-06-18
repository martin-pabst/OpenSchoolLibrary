package de.sp.main.mainframe;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.User;
import de.sp.database.stores.SchoolTermStore;
import de.sp.main.login.LoginServlet;
import de.sp.main.resources.modules.ModuleManager;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.tools.server.BaseServlet;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;

public class MainFrameServlet extends BaseServlet {

	private Template template;

	@Override
	public void init(ServletConfig config) throws ServletException {

		template = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/mainframe/main.vm");

		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Logger logger = LoggerFactory.getLogger(MainFrameServlet.class);

		HttpSession session = req.getSession();

		final User user = (User) session.getAttribute(LoginServlet.USER);

		SchoolTerm schoolTerm = null;
		School school = null;

		String schoolTermURLPart = getLastURLPart(req);

		try {
			if (schoolTermURLPart.startsWith("st")) {

				Long schoolTermId = Long.parseLong(schoolTermURLPart
						.substring(2));

				schoolTerm = SchoolTermStore.getInstance().getSchoolTerm(
						schoolTermId);

				if (schoolTerm == null) {
					throw new Exception("Can't find school_term-record!");
				}

				school = schoolTerm.getSchool();

			} else {
				user.setLastSelectedSchoolTermIfNull();
				schoolTerm = user.getLastSelectedSchoolTerm();
				if(schoolTerm != null){
					school = schoolTerm.getSchool();
				}
			}

			if (school != null && !user.hasAnyPermissionForSchool(school)) {
				throw new Exception(
						"User has no permissions for corresponding school!");
			}

			if (schoolTerm != null && !user.getLast_selected_school_term_id().equals(
					schoolTerm.getId())) {
				user.setLast_selected_school_term_id(schoolTerm.getId());

				Connection con = ConnectionPool.open();
				UserDAO.update(user, con);
			}

		} catch (Exception ex) {
			logger.error("User with id " + user.getId()
					+ " requested school_term with id "
					+ schoolTermURLPart.substring(2) + ". ", ex);

			resp.setContentType("text/html");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		processRequest(req, resp, school, schoolTerm, user,
				logger, session);
	}

	protected void processRequest(HttpServletRequest req,
			HttpServletResponse response, School school, SchoolTerm schoolTerm,
			User user, Logger logger, HttpSession session)
			throws ServletException, IOException {

		TS ts = (TS) session.getAttribute(LoginServlet.TRANSLATION_SERVICE);

		// Configuration config = Configuration.getInstance();

		StringBuilder sbLeft = new StringBuilder();
		StringBuilder sbRight = new StringBuilder();

		Long school_id = school == null ? null : school.getId();
		
		String school_id_s = school == null ? "null" : "" + school.getId();
		String school_term_id_s = schoolTerm == null ? "null" : "" + schoolTerm.getId();
		
		ModuleManager.getMenuHtml(sbLeft, sbRight, user, 4, ts, school_id);

		/* create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put("ts", ts); // language
		context.put("user", user);
		context.put("school", school);
		context.put("menuLeft", sbLeft.toString());
		context.put("menuRight", sbRight.toString());

		context.put("cssImportStatements",
				ModuleManager.getCSSImportStatements("    "));
		context.put("jsImportStatements",
				ModuleManager.getJSImportStatements("    "));

		context.put("schoolIdCode", "var global_school_id = " + school_id_s
				+ ";\n" + "var global_school_term_id = " + school_term_id_s
				+ ";\n");

		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(writer.toString());

	}

}
