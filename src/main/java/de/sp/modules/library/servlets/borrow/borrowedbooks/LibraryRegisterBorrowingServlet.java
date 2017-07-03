package de.sp.modules.library.servlets.borrow.borrowedbooks;

import com.google.gson.Gson;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BorrowsDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.TeacherDAO;
import de.sp.database.model.Borrows;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class LibraryRegisterBorrowingServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new Gson();

		BarcodeInfoRecord bir;

		RegisterBorrowingRequest rbr = gson.fromJson(postData,
				RegisterBorrowingRequest.class);

		try (Connection con = ConnectionPool.beginTransaction()) {

			rbr.validate(ts);

			rbr.normalizeBarcode();

			user.checkPermission(LibraryModule.PERMISSION_BORROW, rbr.getSchool_id());

			if ((rbr.getStudent_id() != null || rbr.getTeacher_id() != null)
					&& !(rbr.getStudent_id() != null && rbr.getTeacher_id() != null)
					&& rbr.getBarcode() != null && !rbr.getBarcode().isEmpty()) {

				bir = LibraryDAO.getBarcodeInfo(rbr.getBarcode(), rbr.getSchool_id(),
						con);
				
				if (bir == null) {
					bir = new BarcodeInfoRecord(BarcodeInfoStatus.error,
							"Unbekannter Barcode: " + rbr.getBarcode().replace("<", "&lt;"));
				} else if (bir.bookIsAlreadyBorrowed()
						&& !rbr.isUnbookFromPreviousBorrower()) {
					bir.setStatusAlreadyBorrowed();
				} else {

					if (bir.bookIsAlreadyBorrowed()) {
						BorrowsDAO.delete(bir.getBorrows_id(), con);
					}

					Calendar cal = Calendar.getInstance();
					Date begin_date = cal.getTime();
					cal.add(Calendar.YEAR, 1); // to get previous year add
												// -1
					Date end_date = cal.getTime();

					checkTeacherStudentBelongToSchool(rbr.getStudent_id(), rbr.getTeacher_id(), rbr.getSchool_id(), con);
					
					Borrows b = BorrowsDAO.insert(bir.getBook_Copy_id(),
							rbr.getStudent_id(), rbr.getTeacher_id(),
							begin_date, end_date, null, null, rbr.getOver_holidays(), con);

					bir.setBorrows_id(b.getId());
					bir.setBegindate(begin_date);
					bir.setEnddate(end_date);

					bir.setStatus(BarcodeInfoStatus.success);

				}

			} else {
				bir = new BarcodeInfoRecord(BarcodeInfoStatus.error,
						"Wrong input parameters.");
			}

			con.commit();

		} catch (Exception ex) {
			logger.error("Error serving Request", ex);
			bir = new BarcodeInfoRecord(BarcodeInfoStatus.error, "Error: "
					+ ex.toString());
		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(gson.toJson(bir));

	}

	private void checkTeacherStudentBelongToSchool(Long student_id,
			Long teacher_id, Long school_id, Connection con) throws Exception {
		
		if(teacher_id != null){
			if(!school_id.equals(TeacherDAO.getSchoolId(teacher_id, con))){
				throw new Exception("Teacher with id " + teacher_id + " does not belong to school with id " + school_id);
			};
		}
		
		if(student_id != null){
			if(!school_id.equals(StudentDAO.getSchoolId(student_id, con))){
				throw new Exception("Student with id " + student_id + " does not belong to school with id " + school_id);
			};
		}
		
	}

}
