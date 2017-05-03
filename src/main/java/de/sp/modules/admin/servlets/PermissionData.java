package de.sp.modules.admin.servlets;

import de.sp.main.resources.permissions.Permission;
import de.sp.main.resources.text.TS;

/**
 * Created by martin on 03.05.2017.
 */
public class PermissionData {

    public String module;

    public String identifier;

    public String description;


    public PermissionData(TS ts, Permission permission){

        identifier = permission.getIdentifier();
        description = ts.get(permission.getDescription());

        int i = identifier.indexOf('.');

        String moduleIdentifier = identifier;

        if (i > 0){
            moduleIdentifier = identifier.substring(0, i);
        }

        module = ts.get(moduleIdentifier);

    }



}
