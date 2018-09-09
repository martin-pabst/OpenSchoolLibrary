package de.sp.modules.admin.servlets.useradministration;

import de.sp.main.services.modules.Permission;
import de.sp.main.services.text.TS;

/**
 * Created by martin on 03.05.2017.
 */
public class PermissionData {

    public String module;

    public String name;

    public String remark;

    public Long id;

    public PermissionData(TS ts, Permission permission){

        name = permission.getName();
        remark = ts.get(permission.getRemark());

        id = permission.getId();

        int i = name.indexOf('.');

        String modulename = name;

        if (i > 0){
            modulename = name.substring(0, i);
        }

        module = ts.get(modulename + ".moduleName");

    }



}
