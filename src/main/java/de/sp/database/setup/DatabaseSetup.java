package de.sp.database.setup;

import de.sp.database.connection.ConnectionPool;
import de.sp.main.config.Configuration;
import de.sp.tools.database.schema.SchemaFromXMLGenerator;
import de.sp.tools.server.progressServlet.ProgressServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class DatabaseSetup {

	public static void main(String[] args) {
		DatabaseSetup databaseSetup = new DatabaseSetup();
		databaseSetup.setup(true, null);
	}

	public void setup(boolean deleteOldSchema, String progressCode) {

		SchemaFromXMLGenerator generator = new SchemaFromXMLGenerator();

		try {

			Logger logger = LoggerFactory.getLogger(this.getClass());

			generator.readFromFile("/database/databaseschema.xml");

			Configuration config = Configuration.getInstance();

			ConnectionPool.init(config);

			ArrayList<String> statements = generator.getSchema()
					.toSQLStatements(config.getDatabase().getDatabaseType());

			try (Connection con = ConnectionPool.open()) {

				try {
					con.createQuery("drop schema public cascade")
							.executeUpdate();
				} catch (Exception ex) {

				}

				con.createQuery("create schema public").executeUpdate();

				int i = 0;

				for (String statement : statements) {

					if (progressCode == null) {
						logger.info(statement);
					} else {

						ProgressServlet.publishProgress(0, statements.size(), i,
								"Tabelle " + i + " von " + statements.size(),
								false, "", progressCode);

					}

					con.createQuery(statement).executeUpdate();

					i++;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e.toString());
		}
	}
}
