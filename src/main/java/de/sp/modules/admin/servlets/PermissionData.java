package de.sp.modules.admin.servlets;

import de.sp.main.resources.modules.Permission;
import de.sp.main.resources.text.TS;

/**
 * Created by martin on 03.05.2017.
 */
public class PermissionData {

    public String module;

    public String name;

    public String remark;


    public PermissionData(TS ts, Permission permission){

        name = permission.getName();
        remark = ts.get(permission.getRemark());

        int i = name.indexOf('.');

        String modulename = name;

        if (i > 0){
            modulename = name.substring(0, i);
        }

        module = ts.get(modulename);

    }



}
