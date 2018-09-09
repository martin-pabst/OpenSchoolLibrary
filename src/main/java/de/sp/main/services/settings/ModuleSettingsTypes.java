package de.sp.main.services.settings;

public class ModuleSettingsTypes {

    private Class userSettingsType;
    private Class schoolSettingsType;
    private Class systemSettingsType;

    public ModuleSettingsTypes(Class userSettingsType, Class schoolSettingsType, Class systemSettingsType) {
        this.userSettingsType = userSettingsType;
        this.schoolSettingsType = schoolSettingsType;
        this.systemSettingsType = systemSettingsType;
    }

    public Class getUserSettingsType() {
        return userSettingsType;
    }

    public Class getSchoolSettingsType() {
        return schoolSettingsType;
    }

    public Class getSystemSettingsType() {
        return systemSettingsType;
    }
}
