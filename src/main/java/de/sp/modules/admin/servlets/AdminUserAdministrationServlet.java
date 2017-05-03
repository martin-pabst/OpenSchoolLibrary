package de.sp.modules.admin.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.RoleDAO;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.daos.basic.UserRoleDAO;
import de.sp.database.model.Role;
import de.sp.database.model.User;
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

    private AddRemoveRoleResponse addRemoveRole(AddRemoveRoleRequest arr, Connection con) {
    
        if(arr.addRemove == null){
            return new AddRemoveRoleResponse("error", "Wrong parameter value");
        }
        
        boolean isAddRole = arr.addRemove.equals("add");
    
        User user = UserDAO.getUserById(arr.user_id, con);
        Role role = RoleDAO.getRoleById(arr.role_id, con);
        
        if(!user.getSchool_id().equals(arr.school_id)){
            return new AddRemoveRoleResponse("error", "User has no Permission to alter other user from this school.");
        }

        if(role.getSchool_id() != arr.school_id){
            return new AddRemoveRoleResponse("error", "User has no Permission to alter role from this school.");
        }

        if(isAddRole){
            UserRoleDAO.addRole(arr.user_id, arr.role_id, con);
        } else {
            UserRoleDAO.removeRole(arr.user_id, arr.role_id, con);
        }

        return new AddRemoveRoleResponse("success", "");
    
    }


}
