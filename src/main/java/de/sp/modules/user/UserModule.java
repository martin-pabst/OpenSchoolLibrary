package de.sp.modules.user;

import de.sp.main.services.settings.ModuleSettingsTypes;
import org.apache.velocity.Template;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.services.modules.Module;
import de.sp.main.services.templates.VelocityEngineFactory;
import de.sp.main.services.text.TS;

public class UserModule extends Module {

	public static final String USER = "user";
	public static final String USEROPEN = "user.open";


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
		return new String[] {USEROPEN};
	}

	@Override
	public String getIdentifier() {
		return USER;
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
		m.addItem(new MenuItem("user.menu.settings", "userSettings", "fa-cog",
				null, null, null, 20));

		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return null;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
                                Long school_id, StringBuilder sb) {

		switch (fragmentId) {
		case "logout":
			renderTemplate(logoutTemplate, ts, user, school_id, sb);
			break;

		case "userSettings":
			renderTemplate(settingsTemplate, ts, user, school_id, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "logout", "userSettings" };
	}

	@Override
	public ModuleSettingsTypes getModuleSettingsTypes() {
		return null;
	}

}
