package de.sp.modules.admin.servlets;

import de.sp.main.resources.modules.ModuleManager;
import de.sp.main.resources.modules.Permission;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.daos.UserAdministrationDAO;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 03.05.2017.
 */
public class UserAdministrationListsResponse {

    public List<UserData> users;
    public List<RoleData> roles;
    public List<PermissionData> permissions;

    public UserAdministrationListsResponse(Connection con, TS ts, Long school_id){

        users = UserAdministrationDAO.getUserList(con, school_id);
        roles = UserAdministrationDAO.getRolesList(con, school_id, ts);

        List<Permission> rawPermissions = ModuleManager.getAllPermissions();

        permissions = new ArrayList<>();

        for (Permission rawPermission : rawPermissions) {
            PermissionData pd = new PermissionData(ts, rawPermission);
            permissions.add(pd);
        }

    }



}
