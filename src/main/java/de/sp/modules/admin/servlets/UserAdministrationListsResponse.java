package de.sp.modules.admin.servlets;

import de.sp.main.resources.text.TS;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by martin on 03.05.2017.
 */
public class UserAdministrationListsResponse {

    public List<UserData> users;
    public List<RoleData> roles;
    public List<PermissionData> permissions;

    public UserAdministrationListsResponse(Connection con, TS ts){





    }



}
