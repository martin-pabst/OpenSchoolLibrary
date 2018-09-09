package de.sp.modules.install;

import de.sp.main.services.settings.ModuleSettingsTypes;
import org.eclipse.jetty.servlet.ServletContextHandler;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.services.modules.Module;
import de.sp.main.services.text.TS;
import de.sp.modules.install.servlets.InitDBServlet;

public class InstallModule extends Module {

	public InstallModule() {

		setMayGetDeactivated(false);

		setInstalled(true);

	}

	@Override
	public String[] getPermissionNames() {
		return new String[] {};
	}

	@Override
	public String getIdentifier() {
		return "install";
	}

	@Override
	public String getName() {
		return "install.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		return new MenuItem[] {};

	}

	@Override
	public String getMinimumPermission() {
		return null;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
                                Long school_id, StringBuilder sb) {

	}

	@Override
	public void addServlets(ServletContextHandler context) {
		context.addServlet(InitDBServlet.class, "/install");
	}

	@Override
	public String[] addFragmentIds() {
		return new String[] {};
	}

	@Override
	public ModuleSettingsTypes getModuleSettingsTypes() {
		return null;
	}

}
