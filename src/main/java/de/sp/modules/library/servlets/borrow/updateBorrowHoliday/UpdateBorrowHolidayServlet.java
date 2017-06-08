package de.sp.modules.library.servlets.borrow.updateBorrowHoliday;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BorrowsDAO;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UpdateBorrowHolidayServlet extends BaseServlet {

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

                    case "updateHoliday":

                        UpdateBorrowHolidayRequest ubhr = gson.fromJson(postData, UpdateBorrowHolidayRequest.class);

                        ubhr.validate(ts);

                        user.checkPermission(LibraryModule.PERMISSION_BORROW,
                                ubhr.school_id);

                        responseString = gson.toJson(updateBorrowHoliday(ubhr, user, con));

                        break;

                }

                con.commit(true);

            } catch (Exception ex) {
                logger.error("Error serving Request", ex);
                con.rollback();
                responseString = gson.toJson(new UpdateBorrowHolidayResponse("error", "Fehler: " + ex.toString()));
            }

        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(responseString);

    }

    private UpdateBorrowHolidayResponse updateBorrowHoliday(UpdateBorrowHolidayRequest ubhr, User user, Connection con) {

        BorrowsDAO.setOverHolidays(ubhr.id, ubhr.value_new, con);

        return new UpdateBorrowHolidayResponse("success", "");

    }


}
