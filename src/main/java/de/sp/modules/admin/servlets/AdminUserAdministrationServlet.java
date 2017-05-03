package de.sp.modules.admin.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.TeacherDAO;
import de.sp.database.model.Student;
import de.sp.database.model.Teacher;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.AdminModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.daos.LibrarySettingsDAO;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsRequest;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsResponse;
import de.sp.modules.library.servlets.settings.MergeStudentsRequest;
import de.sp.modules.library.servlets.settings.MergeStudentsResponse;
import de.sp.protocols.w2ui.grid.gridrequest.GridRequestGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseGet;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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



}
