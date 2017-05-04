package de.sp.modules.admin.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.daos.basic.UserRoleDAO;
import de.sp.database.model.Role;
import de.sp.database.model.User;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.AdminModule;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsResponse;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminUserAdministrationServlet extends BaseServlet {

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

                        UserAdministrationListsRequest ualr = gson.fromJson(postData, UserAdministrationListsRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION,
                                ualr.school_id);

                        UserAdministrationListsResponse ualresp = new UserAdministrationListsResponse(con, ts, ualr.school_id);

                        responseString = gson.toJson(ualresp);

                        break;

                    case "addRemoveRole":

                        AddRemoveRoleRequest arr = gson.fromJson(postData, AddRemoveRoleRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION,
                                arr.school_id);

                        responseString = gson.toJson(addRemoveRole(arr, con));


                        break;

                    case "saveUser":

                        SaveUserRequest sur = gson.fromJson(postData, SaveUserRequest.class);

                        user.checkPermission(AdminModule.PERMISSIONADMINUSERADMINISTRATION,
                                sur.school_id);

                        if(sur.record.id != null){
                            responseString = gson.toJson(updateUser(sur, con));
                        } else {
                            responseString = gson.toJson(saveUser(sur, con));
                        }


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

    private SaveUserResponse updateUser(SaveUserRequest sur, Connection con) throws Exception {

        UserRolePermissionStore ups = UserRolePermissionStore.getInstance();

        SaveUserRecord record = sur.record;

        User user = ups.getUserById(record.id);

        if(user == null){
            return new SaveUserResponse("error", "No user with given id exists.", null);
        }

        if(!user.getSchool_id().equals(sur.school_id)){
            return new SaveUserResponse("error", "No user with given id exists in given school.", null);
        }

        User otherUserWithNewName = ups.getUserBySchoolIdAndName(sur.school_id, record.username);

        if(otherUserWithNewName != null){
            return new SaveUserResponse("error", "User with given name already exists in this school.", null);
        }

        if(record.name != null && !record.name.isEmpty()) {
            user.setName(record.name);
        }

        if(record.username != null && !record.username.isEmpty()){
            user.setUsername(record.username);
        }

        user.setIs_admin(record.is_admin == 1);

        if(record.password != null && !record.password.isEmpty()){
            user.setPassword(record.password);
        }

        UserDAO.update(user, con);
        record.password = "";

        return new SaveUserResponse("success", "", record);

    }

    private SaveUserResponse saveUser(SaveUserRequest sur, Connection con) throws Exception {

        SaveUserRecord record = sur.record;

        User oldUser = UserRolePermissionStore.getInstance().getUserBySchoolIdAndName(sur.school_id, record.username);
        if(oldUser != null){
            return new SaveUserResponse("error", "User with given name already exists.", null);
        }

        User user = UserDAO.insert(record.username, record.name,
                    record.password, "de-DE", null, record.is_admin == 1, sur.school_id, con);

        UserRolePermissionStore.getInstance().addUser(user);

        record.id = user.getId();

        return new SaveUserResponse("success", "", record);

    }

    private AddRemoveRoleResponse addRemoveRole(AddRemoveRoleRequest arr, Connection con) {
    
        if(arr.addRemove == null){
            return new AddRemoveRoleResponse("error", "Wrong parameter value");
        }
        
        boolean isAddRole = arr.addRemove.equals("add");

        UserRolePermissionStore userRolePermissionStore = UserRolePermissionStore.getInstance();

        User user = userRolePermissionStore.getUserById(arr.user_id);
        Role role = userRolePermissionStore.getRoleById(arr.role_id);

        if(user == null || role == null){
            return new AddRemoveRoleResponse("error", "User or role with given id is not known.");
        }

        if(!user.getSchool_id().equals(arr.school_id)){
            return new AddRemoveRoleResponse("error", "User has no Permission to alter other user from this school.");
        }

        if(role.getSchool_id() != arr.school_id){
            return new AddRemoveRoleResponse("error", "User has no Permission to alter role from this school.");
        }

        if(isAddRole){

            userRolePermissionStore.addRoleToUser(role, user, con);

        } else {
            userRolePermissionStore.removeRoleFromUser(role, user, con);
        }

        return new AddRemoveRoleResponse("success", "");
    
    }


}
