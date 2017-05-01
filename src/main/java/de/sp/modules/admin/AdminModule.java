package de.sp.modules.admin;

import org.apache.velocity.Template;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;

public class AdminModule extends Module {

	public static final String PERMISSIONADMIN = "admin";
	private Template userAdministrationTemplate;

	public AdminModule() {

		setMayGetDeactivated(true);
		setInstalled(true);

		userAdministrationTemplate = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/admin/userAdministration.vm");

	}

	@Override
	public String[] getPermissionNames() {

		return new String[] { PERMISSIONADMIN };

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

		m.addItem(new MenuItem("admin.menu.useradministration", "startUserAdministration", "fa-institution",
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
		return new String[] {};
	}

}
