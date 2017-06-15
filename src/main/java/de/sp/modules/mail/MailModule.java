package de.sp.modules.mail;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.text.TS;

public class MailModule extends Module {

	public static final String MAILPERMISSIONWRITE = "mail.write";
	public static final String PERMISSIONMAIL = "mail";

	public MailModule() {
		setMayGetDeactivated(false);
		setInstalled(true);
	}

	@Override
	public String[] getPermissionNames() {
		
		return new String[]{
				MAILPERMISSIONWRITE, PERMISSIONMAIL
		};
		
	}
	
	@Override
	public String getIdentifier() {
		return PERMISSIONMAIL;
	}

	@Override
	public String getName() {
		return "mail.moduleName";
	}

	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("", "startMail",
				"fa-envelope-o", null, new String[] { PERMISSIONMAIL }, PERMISSIONMAIL,
				MenuItemSide.right, 200);

		return new MenuItem[] { m };

	}

	@Override
	public String getMinimumPermission() {
		return PERMISSIONMAIL;
	}
	
	
	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
                                Long school_id, StringBuilder sb) {
		
		switch (fragmentId) {
		case "startMail":  // TODO
//			renderTemplate(adminTemplate, ts, user, sb);
			break;

		default:
			break;
		}
		
		
	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "startMail" };
	}


}
