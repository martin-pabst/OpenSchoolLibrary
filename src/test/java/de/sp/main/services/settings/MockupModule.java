package de.sp.main.services.settings;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.services.modules.Module;
import de.sp.main.services.text.TS;

public class MockupModule extends Module{
    @Override
    public String getIdentifier() {
        return "mockupModule";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getPermissionNames() {
        return new String[0];
    }

    @Override
    public MenuItem[] getMenuItems() {
        return new MenuItem[0];
    }

    @Override
    public void getHtmlFragment(String fragmentId, TS ts, User user, Long school_id, StringBuilder sb) {

    }

    @Override
    public String[] addFragmentIds() {
        return new String[0];
    }

    @Override
    public ModuleSettingsTypes getModuleSettingsTypes() {
        return new ModuleSettingsTypes(TestSettings.class, null, null);
    }
}
