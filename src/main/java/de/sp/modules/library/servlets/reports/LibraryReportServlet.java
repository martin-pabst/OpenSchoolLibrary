package de.sp.modules.library.servlets.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.servlets.reports.model.BaseReport;
import de.sp.modules.library.servlets.reports.model.ContentType;
import de.sp.modules.library.servlets.reports.model.ReportManager;
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
import java.io.OutputStream;
import java.net.URLDecoder;

public class LibraryReportServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

		String postString = URLDecoder.decode(postData, "UTF-8");
		postString = postString.substring(postString.indexOf('{'));

		ExecuteReportRequest executeReportRequest = gson.fromJson(postString, ExecuteReportRequest.class);

		try (Connection con = ConnectionPool.beginTransaction()) {

			user.checkPermission(LibraryModule.PERMISSION_REPORTS,
					executeReportRequest.school_id);

			BaseReport report = ReportManager.getInstance().getReport(executeReportRequest.getDataType(), executeReportRequest.getReportId());
			byte[] reportData = executeReport(report, executeReportRequest, con);

			con.commit();

			response.setContentType(executeReportRequest.getContentTypeEnum().getContentType());
			response.setStatus(HttpServletResponse.SC_OK);
			OutputStream outStream = response.getOutputStream();

			ContentType contentType = executeReportRequest.getContentTypeEnum();

			if(!(contentType == ContentType.html)) {
//				response.setHeader("Content-Disposition", "attachment, filename=\"" + report.getFilename() + "." + contentType.getFileEnding() + "\"");
			}

			response.setContentLength(reportData.length);
			outStream.write(reportData,0,reportData.length);

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

	private byte[] executeReport(BaseReport report, ExecuteReportRequest executeReportRequest, Connection con) throws IOException, JRException {

		return report.execute(executeReportRequest.getContentTypeEnum(), executeReportRequest.selectedRows, executeReportRequest.school_id,
				executeReportRequest.school_term_id, con);

	}



}
