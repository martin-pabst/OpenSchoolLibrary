package de.sp.main;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.StoreManager;
import de.sp.database.statements.StatementStore;
import de.sp.main.config.Configuration;
import de.sp.main.config.DatabaseConfig;
import de.sp.main.login.LoginServlet;
import de.sp.main.mainframe.MainFrameServlet;
import de.sp.main.mainframe.definitionsservlet.DefinitionsServlet;
import de.sp.main.resources.htmlfragments.HtmlFragmentServlet;
import de.sp.main.resources.modules.ModuleManager;
import de.sp.main.resources.modules.Plugin;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.main.server.AuthenticationFilter;
import de.sp.tools.server.progressServlet.ProgressServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

public class StartServer {

	public static final String CONNECTION_POOL = "ConnectionPool";

	public static void main(String[] args) {

		try {
			// Konfigurationsdatei configuration.xml einlesen
			Configuration config = Configuration.getInstance();

			// ConnectionPool for database connections
			ConnectionPool.init(config);

			// Initialize template engine
			VelocityEngineFactory.getVelocityEngine();

			// Find and register plugins
			ModuleManager.registerPlugins();

			// read sql-statements from xml files
			StatementStore.readStatements(config.getDatabase().getType());

			try {
				StoreManager.getInstance().loadStoresFromDatabase(
						ModuleManager.getAllPermissions());
			} catch (Exception ex) {
				Logger logger = LoggerFactory.getLogger(StartServer.class);
				logger.error("Error in populating stores: ", ex);
			}

			TS.readTextResources();


			startServer(config);

		} catch (Exception ex) {

			Logger logger = LoggerFactory.getLogger(StartServer.class);
			logger.error("Fataler Fehler beim Start des Servers: ", ex);
			System.exit(1);

		}

	}

	private static void testDatabase(DatabaseConfig database) {





	}

	private static void startServer(Configuration config) throws Exception {

		Server server = new Server(8080);

		//Init servlet context
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addFilter(AuthenticationFilter.class, "/*",
				EnumSet.of(DispatcherType.REQUEST));


		//Memory based session handling
		SessionHandler sessionHandler = new SessionHandler();
		sessionHandler.setSessionTrackingModes(EnumSet
				.of(SessionTrackingMode.COOKIE));

		context.setSessionHandler(sessionHandler);

		// if needed use this to set Attributes on servlet context
		// context.setAttribute(CONNECTION_POOL, connectionPool);

		// Jetty should also serve static files
		ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
		staticHolder.setInitParameter("resourceBase", config.getDirectories()
				.getWebcontentdirectory());
		staticHolder.setInitParameter("pathInfoOnly", "true");
		staticHolder.setInitParameter("dirAllowed", "false");
		// register staticHolder with same context as servlets so that
		// Authenticationfilter is also invoked
		context.addServlet(staticHolder, "/*");

		for (Plugin plugin : ModuleManager.getPlugins()) {

			staticHolder = new ServletHolder(new DefaultServlet());
			staticHolder.setInitParameter("resourceBase", plugin
					.getWebContentDir().toAbsolutePath().toString());
			staticHolder.setInitParameter("pathInfoOnly", "true");
			staticHolder.setInitParameter("dirAllowed", "false");
			context.addServlet(staticHolder,
					"/modules/" + plugin.getIdentifier() + "/*");

		}

		// register basic builtin servlets
		context.addServlet(LoginServlet.class, "/login/*");
		context.addServlet(MainFrameServlet.class, "/main/*"); // e.g.
																// /main/st125#startLibrary
																// for
																// school_term
																// with id = 125
		context.addServlet(ProgressServlet.class, "/progress");
		context.addServlet(DefinitionsServlet.class, "/definitions");
		context.addServlet(HtmlFragmentServlet.class, "/fragments/*");

		// add servlets of modules and plugins
		ModuleManager.addServlets(context);

		// Add default servlet for 404 Error messages
		context.addServlet(DefaultServlet.class, "/");

		server.setHandler(context);

		server.start();
		server.join();

	}

}
