package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BorrowsDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.TeacherDAO;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BarcodeInfoStatus;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BorrowedBookRecord;
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
import java.util.List;

public class LibraryReturnBookServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

//		Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

        ReturnBookResponse returnBookResp;

        ReturnBookRequest rbr = gson
                .fromJson(postData, ReturnBookRequest.class);

        try (Connection con = ConnectionPool.beginTransaction()) {

            try {

                returnBookResp = LibraryDAO.getReturnBookResponse(rbr.getBarcode(),
                        rbr.getSchool_id(), con);

                if (rbr.isPerformReturn()) {

                    user.checkPermission(LibraryModule.PERMISSION_RETURN,
                            rbr.getSchool_id());

                    if (returnBookResp != null
                            && returnBookResp.getBorrows_id() != null
                            && returnBookResp.getReturn_date() == null
                            && (returnBookResp.getStudent_id() != null || returnBookResp
                            .getTeacher_id() != null)
                            && !(returnBookResp.getStudent_id() != null && returnBookResp
                            .getTeacher_id() != null)) {

                        Calendar cal = Calendar.getInstance();
                        Date return_date = cal.getTime();

                        checkTeacherStudentBelongToSchool(returnBookResp.getStudent_id(),
                                returnBookResp.getTeacher_id(), rbr.getSchool_id(), con);

                        BorrowsDAO.setReturnDate(returnBookResp.getBorrows_id(),
                                return_date, con);

                        List<BorrowedBookRecord> bbList = returnBookResp.getBorrowedBooksList();

                        for (int i = 0; i < bbList.size(); i++) {
                            BorrowedBookRecord bb = bbList.get(i);
                            if (bb.getBarcode().equals(rbr.getBarcode())) {
                                bbList.remove(i);
                                break;
                            }
                        }

                        returnBookResp.setStatus(BarcodeInfoStatus.success);

                    } else {
                        if (returnBookResp == null) {
                            returnBookResp = new ReturnBookResponse(BarcodeInfoStatus.error,
                                    "Das Buch mit dem Barcode " + rbr.getBarcode() + " gibt es nicht.");
                        } else {
                            returnBookResp = new ReturnBookResponse(BarcodeInfoStatus.error,
                                    "Das Buch mit dem Barcode " + rbr.getBarcode() + " ist nicht verliehen.");
                        }
                    }

                }

            } catch (Exception ex) {
                logger.error("Error serving Request", ex);
                returnBookResp = new ReturnBookResponse(BarcodeInfoStatus.error, "Error: "
                        + ex.toString());
            }

            con.commit();

        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(gson.toJson(returnBookResp));

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
