package de.sp.modules.root.schooladministration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SchoolDAO;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.model.Role;
import de.sp.database.model.School;
import de.sp.database.model.StoreManager;
import de.sp.database.model.User;
import de.sp.database.stores.SchoolTermStore;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsResponse;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class RootSchoolAdministrationServlet extends BaseServlet {

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

                        SchoolAdministrationListRequest salr = gson.fromJson(postData, SchoolAdministrationListRequest.class);

                        salr.validate(ts);

                        user.checkRoot();

                        SchoolAdministrationListsResponse salresp = new SchoolAdministrationListsResponse(con);

                        responseString = gson.toJson(salresp);

                        break;

                    case "saveSchool":

                        SaveSchoolRequest ssr = gson.fromJson(postData, SaveSchoolRequest.class);

                        ssr.validate(ts);

                        user.checkRoot();

                        if(ssr.record.id != null){
                            responseString = gson.toJson(updateSchool(ssr, con));
                        } else {
                            responseString = gson.toJson(saveSchool(ssr, con));
                        }


                        break;

                    case "removeSchool":

                        RemoveSchoolRequest rsr = gson.fromJson(postData, RemoveSchoolRequest.class);

                        rsr.validate(ts);

                        user.checkRoot();

                        responseString = gson.toJson(removeSchool(rsr, con));

                        break;

                    case "saveAdmin":

                        SaveAdminRequest sar = gson.fromJson(postData, SaveAdminRequest.class);

                        sar.validate(ts);

                        user.checkRoot();

                        if(sar.record.id != null){
                            responseString = gson.toJson(updateAdmin(sar, con));
                        } else {
                            responseString = gson.toJson(saveAdmin(sar, con));
                        }


                        break;

                    case "removeAdmin":

                        RemoveAdminRequest rar = gson.fromJson(postData, RemoveAdminRequest.class);

                        rar.validate(ts);

                        user.checkRoot();

                        responseString = gson.toJson(removeAdmin(rar, con));

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

    private SaveAdminResponse updateAdmin(SaveAdminRequest sar, Connection con) throws Exception {
    
        User admin = UserRolePermissionStore.getInstance().getUserById(sar.record.id);

        if(admin == null){
            return new SaveAdminResponse("error", "Admin with given id " + saveCastToString(sar.record.id) + " not found.", null);
        }

        AdminData record = sar.record;

        admin.setName(record.name);
        admin.setUsername(record.username);
        if(record.password != null && !record.password.isEmpty()) {
            admin.setPassword(record.password);
        }
        
        UserDAO.update(admin, con);

        record.password = ""; // don't send password back to client
        
        return new SaveAdminResponse("success", "", record);//record);

    }

    private SaveAdminResponse saveAdmin(SaveAdminRequest sar, Connection con) throws Exception {

        AdminData record = sar.record;
        User admin = UserDAO.insert(record.username, record.name, record.password, "de-DE", 
                null, false, sar.school_id, con);

        UserRolePermissionStore.getInstance().addUser(admin);

        record.id = admin.getId();
        record.password = ""; // don't send password back to client

        // truly make her/him admin:
        Role adminRole = null;

        for (Role role : UserRolePermissionStore.getInstance().getRoleListBySchoolId(sar.school_id)) {
            if("admin".equals(role.getName())){
                adminRole = role;
                break;
            }
        }

        if(adminRole != null){
            UserRolePermissionStore.getInstance().addRoleToUser(adminRole, admin, con);
        }

        return new SaveAdminResponse("success", "", record);//record);

    }

    private RemoveSchoolResponse removeSchool(RemoveSchoolRequest rur, Connection con) {

        StoreManager.getInstance().removeSchoolFromStores(rur.school_id);
        SchoolDAO.remove(rur.school_id, con);

        return new RemoveSchoolResponse("success", "");

    }

    private RemoveAdminResponse removeAdmin(RemoveAdminRequest rur, Connection con) {

        User admin = UserRolePermissionStore.getInstance().getUserById(rur.admin_id);

        if(admin == null){
            return new RemoveAdminResponse("error", "Admin with given id not found.");
        }

        // removes user from store and database:
        UserRolePermissionStore.getInstance().removeUsers(Arrays.asList(new User[]{admin}), con);

        return new RemoveAdminResponse("success", "");

    }

    private SaveSchoolResponse updateSchool(SaveSchoolRequest srr, Connection con) throws Exception {

        School school = SchoolTermStore.getInstance().getSchoolById(srr.record.id);

        if(school == null){
            return new SaveSchoolResponse("error", "School with given id " + saveCastToString(srr.record.id) + " not found.", null);
        }

        SchoolData record = srr.record;

        school.setName(record.name);
        school.setNumber(record.number);
        school.setAbbreviation(record.abbreviation);

        SchoolDAO.update(school, con);

        return new SaveSchoolResponse("success", "", school);//record);


    }

    private SaveSchoolResponse saveSchool(SaveSchoolRequest srr, Connection con) throws Exception {

        SchoolData record = srr.record;

        School school = SchoolDAO.insert(record.name, record.number, record.abbreviation, con);

        SchoolTermStore.getInstance().addSchool(school);

        SchoolDAO.initSchool(school, con);

        return new SaveSchoolResponse("success", "", school);//record);

    }



}
