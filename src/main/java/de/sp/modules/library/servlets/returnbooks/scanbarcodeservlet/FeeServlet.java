package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import com.google.gson.Gson;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BorrowsDAO;
import de.sp.database.daos.basic.FeeDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.TeacherDAO;
import de.sp.database.model.Fee;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FeeServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new Gson();

		String command = getLastURLPart(request);

		String responseString = "";

		try (Connection con = ConnectionPool.beginTransaction()) {

			try {

				switch (command) {
				case "get":
					responseString = doGet(user, postData, con, gson);
					break;
				case "delete":
					responseString = doDelete(user, postData, con, gson);
					break;
				case "save":
					responseString = doSave(user, postData, con, gson);
					break;
				case "update":
					responseString = doUpdate(user, postData, con, gson);
					break;
				case "paymentsDone":
					responseString = doPaymentsDone(user, postData, con, gson);
					break;

				}

			} catch (Exception ex) {
				logger.error("Error serving Request", ex);
			}

			con.commit();

		}

			response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(responseString);

	}

	private String doGet(User user, String postData, Connection con, Gson gson)
			throws Exception {
		List<FeeRecord> feeList = new ArrayList<>();

		FeeGetRequest r = gson.fromJson(postData, FeeGetRequest.class);

		user.checkPermission(LibraryModule.PERMISSION_LIBRARY, r.getSchool_id());

		checkTeacherStudentBelongToSchool(r.getStudent_id(), r.getTeacher_id(),
				r.getSchool_id(), con);

		if (r.getStudent_id() != null) {
			feeList = FeeDAO.findByStudentId(r.getStudent_id(), true, con);
		}

		if (r.getTeacher_id() != null) {
			feeList = FeeDAO.findByTeacherId(r.getStudent_id(), true, con);
		}

		return gson.toJson(feeList);

	}

	private String doDelete(User user, String postData, Connection con,
			Gson gson) throws Exception {

		FeeDeleteRequest r = gson.fromJson(postData, FeeDeleteRequest.class);

		user.checkPermission(LibraryModule.PERMISSION_RETURN, r.getSchool_id());

		if (!r.getSchool_id()
				.equals(FeeDAO.getSchoolForFee(r.getFee_id(), con))) {
			throw new Exception("Fee with id " + r.getFee_id()
					+ " does not belong to school with id " + r.getSchool_id());
		}

		FeeDAO.delete(r.getFee_id(), con);

		return gson
				.toJson(new FeeDeleteResponse(GridResponseStatus.success, ""));

	}

	private String doSave(User user, String postData, Connection con, Gson gson)
			throws Exception {

		FeeUpdateSaveRequest r = gson.fromJson(postData,
				FeeUpdateSaveRequest.class);

		user.checkPermission(LibraryModule.PERMISSION_RETURN, r.getSchool_id());

		if (!r.getSchool_id().equals(
				BorrowsDAO.findSchoolId(r.getBorrows_id(), con))) {
			throw new Exception("Borrows-record with id " + r.getBorrows_id()
					+ " does not belong to school with id " + r.getSchool_id());
		}

		Fee fee = FeeDAO.insert(r.getBorrows_id(), r.getAmount(),
				r.getRemarks(), r.getPaid_date(), con);

		return gson.toJson(new FeeSaveResponse(GridResponseStatus.success, "",
				fee.getId()));

	}

	private String doUpdate(User user, String postData, Connection con,
			Gson gson) throws Exception {

		FeeUpdateSaveRequest r = gson.fromJson(postData,
				FeeUpdateSaveRequest.class);

		user.checkPermission(LibraryModule.PERMISSION_RETURN, r.getSchool_id());

		if (!r.getSchool_id()
				.equals(FeeDAO.getSchoolForFee(r.getFee_id(), con))) {
			throw new Exception("Fee-record with id " + r.getBorrows_id()
					+ " does not belong to school with id " + r.getSchool_id());
		}

		if (!r.getSchool_id().equals(
				BorrowsDAO.findSchoolId(r.getBorrows_id(), con))) {
			throw new Exception("Borrows-record with id " + r.getBorrows_id()
					+ " does not belong to school with id " + r.getSchool_id());
		}

		Fee fee = new Fee(r.getFee_id(), r.getBorrows_id(), r.getAmount(),
				r.getRemarks(), r.getPaid_date());

		FeeDAO.update(fee, con);

		return gson.toJson(new FeeSaveResponse(GridResponseStatus.success, "",
				fee.getId()));

	}

	private String doPaymentsDone(User user, String postData, Connection con,
			Gson gson) throws Exception {

		PaymentsDoneRequest r = gson.fromJson(postData,
				PaymentsDoneRequest.class);

		user.checkPermission(LibraryModule.PERMISSION_LIBRARY, r.getSchool_id());

		checkTeacherStudentBelongToSchool(r.getStudent_id(), r.getTeacher_id(),
				r.getSchool_id(), con);

		Date paid_date = Calendar.getInstance().getTime();
		
		FeeDAO.paymentsDone(r.getStudent_id(), r.getTeacher_id(), paid_date, con);

		return gson.toJson(new FeeSaveResponse(GridResponseStatus.success, "",
				null));

	}

	private void checkTeacherStudentBelongToSchool(Long student_id,
			Long teacher_id, Long school_id, Connection con) throws Exception {

		if (teacher_id != null) {
			if (!school_id.equals(TeacherDAO.getSchoolId(teacher_id, con))) {
				throw new Exception("Teacher with id " + teacher_id
						+ " does not belong to school with id " + school_id);
			}
			;
		}

		if (student_id != null) {
			if (!school_id.equals(StudentDAO.getSchoolId(student_id, con))) {
				throw new Exception("Student with id " + student_id
						+ " does not belong to school with id " + school_id);
			}
			;
		}

	}

}
