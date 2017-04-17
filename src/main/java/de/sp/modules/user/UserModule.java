package de.sp.modules.user;

import org.apache.velocity.Template;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;

public class UserModule extends Module {

	private Template logoutTemplate, settingsTemplate;

	public UserModule() {

		setMayGetDeactivated(false);

		setInstalled(true);

		logoutTemplate = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/user/logout.vm");

		settingsTemplate = VelocityEngineFactory.getVelocityEngine()
				.getTemplate("templates/modules/user/settings.vm");
	}

	@Override
	public String[] getPermissionNames() {
		return new String[] {};
	}

	@Override
	public String getIdentifier() {
		return "user";
	}

	@Override
	public String getName() {
		return "user.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("", "", "fa-user", null, new String[] {},
				"user", MenuItemSide.right, 400);

		m.addItem(new MenuItem("user.menu.logout", "logout", "fa-sign-out",
				null, null, null, 10));
		m.addItem(new MenuItem("user.menu.settings", "userSettings", "fa-cogs",
				null, null, null, 20));

		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return null;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
			StringBuilder sb) {

		switch (fragmentId) {
		case "logout":
			renderTemplate(logoutTemplate, ts, user, sb);
			break;

		case "userSettings":
			renderTemplate(settingsTemplate, ts, user, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "logout", "userSettings" };
	}

}
