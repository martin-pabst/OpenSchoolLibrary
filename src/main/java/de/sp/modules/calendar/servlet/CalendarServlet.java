package de.sp.modules.calendar.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.Calendar;
import de.sp.database.model.User;
import de.sp.database.stores.CalendarStore;
import de.sp.main.resources.text.TS;
import de.sp.modules.calendar.CalendarModule;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsResponse;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CalendarServlet extends BaseServlet {

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

                    case "fetchEntries":

                        //school_id=1&type=schedule&start=2017-05-01&end=2017-06-12

                        Map<String, String> parameters = decodePostParameters(postData);
                        
                        GetCalendarRequest gcr = new GetCalendarRequest(parameters.get("school_id"), parameters.get("type"),
                                parameters.get("start"), parameters.get("end"));

//                        GetCalendarRequest gcr = gson.fromJson(postData, GetCalendarRequest.class);

                        user.checkPermission(CalendarModule.CALENDAROPEN,
                                gcr.school_id);

                        List<Calendar> gcResponse = fetchCalendarEntries(con, gcr);

                        Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

                        responseString = gson1.toJson(gcResponse);

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

    private List<Calendar> fetchCalendarEntries(Connection con, GetCalendarRequest gcr) {


        return CalendarStore.getInstance().getCalendarRecords(gcr.school_id, gcr.start, gcr.end);

    }


}
