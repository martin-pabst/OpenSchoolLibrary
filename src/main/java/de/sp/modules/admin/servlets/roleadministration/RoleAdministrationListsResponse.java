package de.sp.modules.admin.servlets.roleadministration;

import de.sp.main.resources.modules.ModuleManager;
import de.sp.main.resources.modules.Permission;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.servlets.useradministration.PermissionData;
import de.sp.modules.admin.servlets.useradministration.RoleData;
import de.sp.modules.admin.servlets.useradministration.UserAdministrationDAO;
import de.sp.modules.admin.servlets.useradministration.UserData;
import de.sp.modules.root.RootModule;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 03.05.2017.
 */
public class RoleAdministrationListsResponse {

    public Map<Long, List<UserData>> roleToUserlistMap = new HashMap<>();
    public List<RoleData> roles;
    public List<PermissionData> permissions;

    public RoleAdministrationListsResponse(Connection con, TS ts, Long school_id){

        List<UserData> users = UserAdministrationDAO.getUserList(con, school_id);
        roles = UserAdministrationDAO.getRolesList(con, school_id, ts);

        for (UserData user : users) {

            for (Long role_id : user.role_ids) {
                List<UserData> userListForRole = roleToUserlistMap.get(role_id);
                if(userListForRole == null){
                    userListForRole = new ArrayList<>();
                    roleToUserlistMap.put(role_id, userListForRole);
                }
                userListForRole.add(user);
            }


        }

        List<Permission> rawPermissions = ModuleManager.getAllPermissions();

        permissions = new ArrayList<>();

        for (Permission rawPermission : rawPermissions) {
            if(!rawPermission.getName().startsWith(RootModule.MODULEIDENTIFIER)) {
                PermissionData pd = new PermissionData(ts, rawPermission);
                permissions.add(pd);
            }
        }

    }



}
