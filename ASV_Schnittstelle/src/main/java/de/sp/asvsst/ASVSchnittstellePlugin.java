package de.sp.asvsst;

import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

import de.sp.asvsst.servlet.ASVImportServlet;
import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.resources.modules.Plugin;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;

public class ASVSchnittstellePlugin extends Plugin {

	public static final String ASVSST = "asvsst";
	public static final String PERMISSIONASVSSTOPEN = "asvsst.open";
	private Template template;

	public ASVSchnittstellePlugin() {
		setMayGetDeactivated(true);
		setInstalled(true);

		template = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/asvsst/asvsst.vm");

	}

	@Override
	public String[] getPermissionNames() {

		return new String[] {PERMISSIONASVSSTOPEN};

	}

	@Override
	public String getIdentifier() {
		return ASVSST;
	}

	@Override
	public String getName() {
		return "asvsst.name";
	}

	@Override
	public MenuItem[] getMenuItems() {
		MenuItem m = new MenuItem("asvsst.menu.main", "startAdminASVImport",
				"fa-download", null, new String[] {PERMISSIONASVSSTOPEN},
				PERMISSIONASVSSTOPEN, 2000);

		m.setInsertBelowThisId("admin");

		return new MenuItem[] { m };

	}

	@Override
	public void addServlets(ServletContextHandler context) {
		context.addServlet(ASVImportServlet.class, "/asvimport");
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
                                Long school_id, StringBuilder sb) {

		switch (fragmentId) {
		case "startAdminASVImport":
			renderTemplate(template, ts, user, school_id, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "startAdminASVImport" };
	}

}
