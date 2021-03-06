package de.sp.modules.admin;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.services.modules.Module;
import de.sp.main.services.settings.ModuleSettingsTypes;
import de.sp.main.services.templates.VelocityEngineFactory;
import de.sp.main.services.text.TS;
import de.sp.modules.admin.servlets.roleadministration.AdminRoleAdministrationServlet;
import de.sp.modules.admin.servlets.useradministration.AdminUserAdministrationServlet;
import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class AdminModule extends Module {


	public static final String MODULEIDENTIFIER = "admin";
	public static final String PERMISSIONADMINOPEN = "admin.open";
	public static final String PERMISSIONADMINUSERADMINISTRATION = "admin.userAdministration";
	public static final String PERMISSIONADMINROLEADMINISTRATION = "admin.roleAdministration";



	private Template userAdministrationTemplate;

	public AdminModule() {

		setMayGetDeactivated(true);
		setInstalled(true);

		userAdministrationTemplate = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/admin/userAdministration.vm");

	}

	@Override
	public String[] getPermissionNames() {

		return new String[] {PERMISSIONADMINOPEN, PERMISSIONADMINUSERADMINISTRATION, PERMISSIONADMINROLEADMINISTRATION};

	}

	@Override
	public String getIdentifier() {
		return MODULEIDENTIFIER;
	}

	@Override
	public String getName() {
		return "admin.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("admin.menu.main", "", "fa-wrench", null,
				new String[] {PERMISSIONADMINOPEN}, PERMISSIONADMINOPEN,
				MenuItemSide.right, 100);

		m.addItem(new MenuItem("admin.menu.useradministration", "startUserAdministration", "fa-users",
				null, new String[] {PERMISSIONADMINUSERADMINISTRATION}, PERMISSIONADMINUSERADMINISTRATION,
				10));
		m.addItem(new MenuItem("admin.menu.defaultsettings", "defaultSettings", "fa-cog",
				null, null, null, 20));
		m.addItem(new MenuItem("admin.menu.sytemsettings", "systemSettings", "fa-cogs",
				null, null, null, 20));


		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return PERMISSIONADMINOPEN;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
                                Long school_id, StringBuilder sb) {

		switch (fragmentId) {
		case "startUserAdministration":
			renderTemplate(userAdministrationTemplate, ts, user, school_id, sb);
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
	public ModuleSettingsTypes getModuleSettingsTypes() {
		return null;
	}

	@Override
    public void addServlets(ServletContextHandler context) {
        context.addServlet(AdminUserAdministrationServlet.class, "/admin/userAdministration/*");
        context.addServlet(AdminRoleAdministrationServlet.class, "/admin/roleAdministration/*");
    }

    }
