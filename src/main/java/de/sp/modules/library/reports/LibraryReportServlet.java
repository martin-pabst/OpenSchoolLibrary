package de.sp.modules.library.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.reports.model.BaseReport;
import de.sp.modules.library.reports.model.ContentType;
import de.sp.modules.library.reports.model.ReportManager;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseSave;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.tools.server.BaseServlet;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;

public class LibraryReportServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();


        String postString = URLDecoder.decode(postData, "UTF-8");

        if (postString.indexOf("{") < 0) {
            response.getWriter().println("<h1>Fehler:<h1>Es wurde kein Bericht ausgewählt.");

        } else {

            try (Connection con = ConnectionPool.beginTransaction()) {


                postString = postString.substring(postString.indexOf('{'));
                ExecuteReportRequest executeReportRequest = gson.fromJson(postString, ExecuteReportRequest.class);


                user.checkPermission(LibraryModule.PERMISSION_REPORTS,
                        executeReportRequest.school_id);

                BaseReport report = ReportManager.getInstance().getReport(executeReportRequest.getDataType(), executeReportRequest.getReportId());

                response.setStatus(HttpServletResponse.SC_OK);

                ContentType contentType = executeReportRequest.getContentTypeEnum();
                response.setContentType(contentType.getContentType());

                if (!(contentType == ContentType.html)) {
                    response.setHeader("Content-Disposition", "filename=\"" + report.getFilename() + "." + contentType.getFileEnding() + "\"");
                }

                executeReport(report, executeReportRequest, con, response);

                con.rollback();

            } catch (Exception ex) {
                logger.error(this.getClass().toString() + ": Error serving data",
                        ex);
                String responseString = gson.toJson(new GridResponseSave(
                        GridResponseStatus.error, ex.toString(), null));

                response.setContentType("text/json");
                response.setStatus(HttpServletResponse.SC_OK);

                response.getWriter().println(responseString);
            }

        }

    }

    private void executeReport(BaseReport report, ExecuteReportRequest executeReportRequest, Connection con, HttpServletResponse response) throws IOException, JRException {

        report.execute(executeReportRequest.getContentTypeEnum(), executeReportRequest.selectedRows,
                executeReportRequest.school_id,
                executeReportRequest.school_term_id, executeReportRequest.getParameterValues(), con, response);

    }


}
