package de.sp.modules.root;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.modules.root.schooladministration.RootSchoolAdministrationServlet;
import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class RootModule extends Module {


	public static final String MODULEIDENTIFIER = "root";
	public static final String PERMISSIONROOTOPEN = "root.open";
	public static final String PERMISSIONROOTSCHOOLADMINISTRATION = "root.schoolAdministration";



	private Template schoolAdministrationTemplate;

	public RootModule() {

		setMayGetDeactivated(true);
		setInstalled(true);

		schoolAdministrationTemplate = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/root/schoolAdministration.vm");

	}

	@Override
	public String[] getPermissionNames() {

		return new String[] {PERMISSIONROOTOPEN, PERMISSIONROOTSCHOOLADMINISTRATION};

	}

	@Override
	public String getIdentifier() {
		return MODULEIDENTIFIER;
	}

	@Override
	public String getName() {
		return "root.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("root.menu.schools", "startSchoolAdministration", "fa-institution", null,
				new String[] {PERMISSIONROOTSCHOOLADMINISTRATION}, PERMISSIONROOTSCHOOLADMINISTRATION,
				MenuItemSide.right, 100);

		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return PERMISSIONROOTOPEN;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
                                Long school_id, StringBuilder sb) {

		switch (fragmentId) {
		case "startSchoolAdministration":
			renderTemplate(schoolAdministrationTemplate, ts, user, school_id, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] {"startSchoolAdministration"};
	}

    @Override
    public void addServlets(ServletContextHandler context) {
        context.addServlet(RootSchoolAdministrationServlet.class, "/root/schoolAdministration/*");
    }

    }
