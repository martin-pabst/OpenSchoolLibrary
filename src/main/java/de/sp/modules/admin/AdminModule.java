package de.sp.modules.admin;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.servlets.AdminUserAdministrationServlet;
import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class AdminModule extends Module {

	public static final String PERMISSIONADMIN = "admin";
	public static final String PERMISSIONADMINUSERADMINISTRATION = "admin.userAdministration";

	private Template userAdministrationTemplate;

	public AdminModule() {

		setMayGetDeactivated(true);
		setInstalled(true);

		userAdministrationTemplate = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/admin/userAdministration.vm");

	}

	@Override
	public String[] getPermissionNames() {

		return new String[] { PERMISSIONADMIN, PERMISSIONADMINUSERADMINISTRATION };

	}

	@Override
	public String getIdentifier() {
		return PERMISSIONADMIN;
	}

	@Override
	public String getName() {
		return "admin.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("admin.menu.main", "", "fa-wrench", null,
				new String[] { PERMISSIONADMIN }, PERMISSIONADMIN,
				MenuItemSide.right, 100);

		m.addItem(new MenuItem("admin.menu.useradministration", "startUserAdministration", "fa-users",
				null, new String[] { PERMISSIONADMIN }, PERMISSIONADMIN,
				10));

		m.addItem(new MenuItem("admin.menu.schools", "", "fa-institution", null,
				new String[] { PERMISSIONADMIN }, PERMISSIONADMIN,
				20));


		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return PERMISSIONADMIN;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
			StringBuilder sb) {

		switch (fragmentId) {
		case "startUserAdministration":
			renderTemplate(userAdministrationTemplate, ts, user, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] {"startUserAdministration"};
	}

    @Override
    public void addServlets(ServletContextHandler context) {
        context.addServlet(AdminUserAdministrationServlet.class, "/admin/userAdministration/*");
    }

    }
