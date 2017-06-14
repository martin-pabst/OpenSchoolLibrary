package de.sp.modules.calendar;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.modules.calendar.servlet.CalendarServlet;
import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class CalendarModule extends Module {

	public static final String CALENDAR = "calendar";
	public static final String CALENDAROPEN = "calendar.open";
	public static final String CALENDARWRITE = "calendar.write";

	private Template template;

	public CalendarModule() {
		setMayGetDeactivated(false);
		setInstalled(true);

		template = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/calendar/calendar.vm", "utf-8");

	}

	@Override
	public String[] getPermissionNames() {
		return new String[] {CALENDAROPEN, CALENDARWRITE};
	}

	@Override
	public String getIdentifier() {
		return CALENDAR;
	}

	@Override
	public String getName() {
		return "calendar.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("", "startCalendar", "fa-calendar", null,
				new String[] {CALENDAROPEN}, CALENDAR,
				MenuItemSide.right, 300);

		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return CALENDAROPEN;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
			StringBuilder sb) {

		switch (fragmentId) {
		case "startCalendar":
			renderTemplate(template, ts, user, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "startCalendar" };
	}

	@Override
	public void addServlets(ServletContextHandler context) {
		context.addServlet(CalendarServlet.class, "/calendar/*");
	}

}
