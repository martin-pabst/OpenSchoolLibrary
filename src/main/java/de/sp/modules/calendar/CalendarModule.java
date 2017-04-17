package de.sp.modules.calendar;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.text.TS;

public class CalendarModule extends Module {

	public static final String PERMISSIONCALENDAR = "calendar";

	public CalendarModule() {
		setMayGetDeactivated(false);
		setInstalled(true);
	}

	@Override
	public String[] getPermissionNames() {
		return new String[] { PERMISSIONCALENDAR };
	}

	@Override
	public String getIdentifier() {
		return PERMISSIONCALENDAR;
	}

	@Override
	public String getName() {
		return "calendar.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("", "startCalendar", "fa-calendar", null,
				new String[] { PERMISSIONCALENDAR }, PERMISSIONCALENDAR,
				MenuItemSide.right, 300);

		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return PERMISSIONCALENDAR;
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
			StringBuilder sb) {

		switch (fragmentId) {
		case "startCalendar": // TODO
			// renderTemplate(adminTemplate, ts, user, sb);
			break;

		default:
			break;
		}

	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "startCalendar" };
	}

}
