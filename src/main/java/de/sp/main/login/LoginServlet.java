package de.sp.main.login;

import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.User;
import de.sp.database.stores.SchoolTermStore;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.main.services.templates.VelocityEngineFactory;
import de.sp.main.services.text.TS;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.string.PasswordSecurity;
import de.sp.tools.string.SaltAndHash;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class LoginServlet extends BaseServlet {

	public static final String USER = "user";

	public static final String TRANSLATION_SERVICE = "TranslationService";

	private Template template;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Logger logger = LoggerFactory.getLogger(LoginServlet.class);

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		School school = null;

		List<School> schools = SchoolTermStore.getInstance().getSchools();
		if(schools.size() == 1){
			school = schools.get(0);
		} else {
			// find school
			String schoolNumber = getLastURLPart(request);
			if(!schoolNumber.isEmpty() && !schoolNumber.equalsIgnoreCase("login")){
				for (School school1 : schools) {
					if(school1.getNumber().equals(schoolNumber)){
						school = school1;
						break;
					}
				}

				if(school == null){
					throw new ServletException("No school found");
				}

			}
		}


		boolean credentialsOK = false;

		User user = null;

		// Configuration config = Configuration.getInstance();

		if (username != null && password != null) {

			try {

				if(school != null) {
					user = UserRolePermissionStore.getInstance().getUserBySchoolIdAndName(
							school.getId(), username);
				}

				// Root user?
				if(user == null && (school == null || schools.size() == 1)){
					user = UserRolePermissionStore.getInstance().getUserBySchoolIdAndName(null, username);
				}


				if (user != null) {

					SaltAndHash saltAndHash = new SaltAndHash(user.getSalt(),
							user.getHash());

					credentialsOK = PasswordSecurity.check(password,
							saltAndHash);
				} else {
					logger.info("User "
							+ username
							+ " tried to log in but username not found in database.");
				}

			} catch (Exception e) {

				logger.error("Fatal exception when checking password: ", e);

			}

			// if (username.equals("admin") && password.equals("123")) {
			//
			// credentialsOK = true;
			//
			// user = new User(1, "admin", "Administrator", "", "",
			// config.getLanguagesConfig().defaultCode);
			//
			// }
		}

		if (credentialsOK) {

			user.setLastSelectedSchoolTermIfNull();
			SchoolTerm schoolTerm = user.getLastSelectedSchoolTerm();

			if (schoolTerm == null && !user.is_root()) {
				response.setContentType("text/html");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println(
						"<h1>User is not registered for any School!</h1>");
				return;
			}

			HttpSession session = request.getSession();

			session.setAttribute(USER, user);

			TS ts = new TS(user.getLanguageCode());
			session.setAttribute(TRANSLATION_SERVICE, ts);

			if (schoolTerm != null) {
				response.sendRedirect("/main/st" + schoolTerm.getId());
			} else {
				response.sendRedirect("/main");
			}

		} else {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Credentials wrong!</h1>");
		}

	}

	@Override
	public void init(ServletConfig config) throws ServletException {

		template = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/login/login.vm");

		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {


		String logoutParamValue = req.getParameter("logout");

		if(logoutParamValue != null) {
			HttpSession session = req.getSession(false);
			if(session != null) {
				session.invalidate();
			}
		}

		/* create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put("TS", new TS(null)); // default language
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		/* show the World */

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(writer.toString());

	}

}
