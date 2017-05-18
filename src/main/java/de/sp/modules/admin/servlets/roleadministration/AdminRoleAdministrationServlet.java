package de.sp.modules.admin.servlets.roleadministration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.RoleDAO;
import de.sp.database.model.Role;
import de.sp.database.model.User;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.main.resources.modules.Permission;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.AdminModule;
import de.sp.modules.admin.servlets.useradministration.RoleData;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsResponse;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminRoleAdministrationServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

        String responseString = "";

        String command = getLastURLPart(request);

        try (Connection con = ConnectionPool.beginTransaction()) {

            try {

                switch (command) {

                    case "getLists":

                        RoleAdministrationListRequest ralr = gson.fromJson(postData, RoleAdministrationListRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION, ralr.school_id);

                        RoleAdministrationListsResponse ralresp = new RoleAdministrationListsResponse(con, ts, ralr.school_id);

                        responseString = gson.toJson(ralresp);

                        break;

                    case "addRemovePermission":

                        AddRemovePermissionRequest arp = gson.fromJson(postData, AddRemovePermissionRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION,
                                arp.school_id);

                        responseString = gson.toJson(addRemovePermission(arp, con));


                        break;

                    case "saveRole":

                        SaveRoleRequest srr = gson.fromJson(postData, SaveRoleRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION,
                                srr.school_id);

                        if(srr.record.id != null){
                            responseString = gson.toJson(updateRole(srr, con));
                        } else {
                            responseString = gson.toJson(saveRole(srr, con));
                        }


                        break;

                    case "removeRoles":

                        RemoveRoleRequest rrr = gson.fromJson(postData, RemoveRoleRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION,
                                rrr.school_id);

                        responseString = gson.toJson(removeRoles(rrr, con));

                        break;

                }

                con.commit(true);

            } catch (Exception ex) {
                logger.error("Error serving Request", ex);
                con.rollback();
                responseString = gson.toJson(new DeleteOldRecordsResponse("error", "Fehler: " + ex.toString()));
            }

        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(responseString);

    }

    private RemoveRoleResponse removeRoles(RemoveRoleRequest rur, Connection con) {

        List<Role> rolesToRemove = new ArrayList<>();
        UserRolePermissionStore ups = UserRolePermissionStore.getInstance();

        //Check if users belong to correct school
        for (Long role_id : rur.role_ids) {
            Role role = ups.getRoleById(role_id);
            if(role.getSchool_id() != rur.school_id){
                return new RemoveRoleResponse("error", "You have no permission to remove this role as it belongs to other school.");
            } else {
                rolesToRemove.add(role);
            }
        }

        for (Role role : rolesToRemove) {
            ups.removeRoles(rolesToRemove, con);
        }

        return new RemoveRoleResponse("success", "");

    }

    private SaveRoleResponse updateRole(SaveRoleRequest srr, Connection con) throws Exception {

        UserRolePermissionStore ups = UserRolePermissionStore.getInstance();

        RoleData record = srr.record;

        Role role = ups.getRoleById(record.id);

        if(role == null){
            return new SaveRoleResponse("error", "No role with given id exists.", null);
        }

        if(role.getSchool_id() != srr.school_id){
            return new SaveRoleResponse("error", "No role with given id exists in given school.", null);
        }

        Role otherRoleWithNewName = ups.getRoleBySchoolIdAndName(srr.school_id, record.name);

        if(otherRoleWithNewName != role){
            return new SaveRoleResponse("error", "Role with given name already exists in this school.", null);
        }

        if(record.name != null && !record.name.isEmpty()) {
            role.setName(record.name);
        }

        if(record.remark != null && !record.remark.isEmpty()){
            role.setRemark(record.remark);
        }
        
        RoleDAO.update(role, con);

        return new SaveRoleResponse("success", "", record);

    }

    private SaveRoleResponse saveRole(SaveRoleRequest sur, Connection con) throws Exception {

        RoleData record = sur.record;

        UserRolePermissionStore ups = UserRolePermissionStore.getInstance();


        Role oldRole = ups.getRoleBySchoolIdAndName(sur.school_id, record.name);
        if(oldRole != null){
            return new SaveRoleResponse("error", "Role with given name already exists.", null);
        }

        Role role = RoleDAO.insert(record.name, record.remark, sur.school_id, "", con);

        ups.addRole(role);

        record.id = role.getId();

        return new SaveRoleResponse("success", "", record);

    }

    private AddRemovePermissionResponse addRemovePermission(AddRemovePermissionRequest arr, Connection con) {
    
        if(arr.addRemove == null){
            return new AddRemovePermissionResponse("error", "Wrong value for paramter addRemove");
        }
        
        boolean isAddPermission = arr.addRemove.equals("add");

        UserRolePermissionStore userRolePermissionStore = UserRolePermissionStore.getInstance();

        Role role = userRolePermissionStore.getRoleById(arr.role_id);
        Permission permission = userRolePermissionStore.findPermissionByName(arr.permission_name);

        if(permission == null || role == null){
            return new AddRemovePermissionResponse("error", "Permission or role with given id is not known.");
        }

        if(role.getSchool_id() != arr.school_id){
            return new AddRemovePermissionResponse("error", "User has no Permission to alter role from this school.");
        }


        if(isAddPermission){

            userRolePermissionStore.addPermissionToRole(role, permission, con);

        } else {
            userRolePermissionStore.removePermissionFromRole(role, permission, con);
        }

        return new AddRemovePermissionResponse("success", "");
    
    }


}
