package de.sp.database.testdata.user;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.RoleDAO;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.daos.basic.UserRoleDAO;
import de.sp.database.model.Role;
import de.sp.database.model.School;
import de.sp.database.model.User;
import de.sp.modules.library.LibraryModule;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

public class UserTestdata {

	public static Role teacherRole;

	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting user testdata...");


		try (Connection con = ConnectionPool.open()) {

			School testSchool = SchoolTestdata.exampleSchool;

			User martin = UserDAO.insert("martin", "Martin Pabst", "123",
					"de-DE", null, false, testSchool.getId(),
					con);
			User admin = UserDAO.insert("admin", "Administrator", "123",
					"de-DE", null, false, testSchool.getId(),
					con);
			User root = UserDAO.insert("root", "Root", "123",
					"de-DE", null, true, null,
					con);


			String[] libraryPermissionList = new LibraryModule()
					.getPermissionNames();

			Role librarianRole = RoleDAO.insert("Bibliothekar/in", "Verwalter/in der Lernmittelfreien Bibliothek",
					testSchool.getId(),
					StringUtils.join(libraryPermissionList, "|"), con);

			String teacherPermissions = "library|mail|calendar|testmodule";

			teacherRole = RoleDAO.insert("Lehrkraft", "Lehrkraft der Schule",
					testSchool.getId(), teacherPermissions, con);

			String adminPermissions = "library|mail|calendar.open|calendar.write|admin.open|admin.userAdministration|asvsst.open";

			Role adminRole = RoleDAO.insert("admin", "Administrator",
					testSchool.getId(), adminPermissions, con);


			UserRoleDAO.insert(martin.getId(), teacherRole.getId(), con);
			UserRoleDAO.insert(admin.getId(), adminRole.getId(), con);
			UserRoleDAO.insert(admin.getId(), librarianRole.getId(), con);

		}
	}

}
