package de.sp.modules.library.servlets.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.daos.basic.BorrowsDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.TeacherDAO;
import de.sp.database.model.Student;
import de.sp.database.model.Teacher;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.daos.LibrarySettingsDAO;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.modules.library.servlets.inventory.copies.BookCopyInfoRecord;
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
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class LibrarySettingsServlet extends BaseServlet {

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

                    case "deleteOldBookings":

                        DeleteOldRecordsRequest dobr = gson.fromJson(postData, DeleteOldRecordsRequest.class);

                        user.checkPermission("library.settings",
                                dobr.school_id);

                        responseString = gson.toJson(deleteOldBookings(dobr, con));

                        break;

                    case "deleteResignedStudents":

                        DeleteOldRecordsRequest dobr1 = gson.fromJson(postData, DeleteOldRecordsRequest.class);

                        user.checkPermission("library.settings",
                                dobr1.school_id);

                        responseString = gson.toJson(deleteResignedStudents(dobr1, con));

                        break;

                    case "deleteResignedTeachers":

                        DeleteOldRecordsRequest dobr2 = gson.fromJson(postData, DeleteOldRecordsRequest.class);

                        user.checkPermission("library.settings",
                                dobr2.school_id);

                        responseString = gson.toJson(deleteResignedTeachers(dobr2, con));

                        break;
                    case "getStudentList":

                        GridRequestGet getData = gson.fromJson(postData,
                                GridRequestGet.class);

                        user.checkPermission("library.settings",
                                getData.getSchool_id());

                        responseString = gson.toJson(getStudentList(getData, con));

                        break;
                    case "mergeStudents":

                        MergeStudentsRequest msr = gson.fromJson(postData,
                                MergeStudentsRequest.class);

                        user.checkPermission("library.settings",
                                msr.school_id);

                        responseString = gson.toJson(mergeStudents(msr, con));

                        break;

                    case "sortOut":
                        SortOutRequest sor = gson.fromJson(postData, SortOutRequest.class);

                        user.checkPermission("library.settings",
                                sor.school_id);

                        responseString = gson.toJson(sortOutBookCopy(sor, con));

                        break;

                    case "changeBarcode":

                        ChangeBarcodeRequest cbr = gson.fromJson(postData, ChangeBarcodeRequest.class);

                        user.checkPermission("library.settings",
                                cbr.school_id);

                        responseString = gson.toJson(changeBarcode(cbr, con));

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

    private ChangeBarcodeResponse changeBarcode(ChangeBarcodeRequest cbr, Connection con) {

        List<BookCopyInfoRecord> bookCopyInfoRecords = BookCopyDAO.getBookCopyInInfo(cbr.school_id, cbr.oldBarcode, con);

        String info = "";
        if(bookCopyInfoRecords.size() > 0) {
            BookCopyInfoRecord bir = bookCopyInfoRecords.get(0);

            info += "Der Barcode des Buchs " + bir.getTitle() + " wurde geändert.check_mark<br />";
            info += "Alter Barcode " + cbr.oldBarcode + "<br />";
            info += "Neuer Barcode " + cbr.newBarcode + "<br />";

            if(bir.getSurname() != null){
                info += "Das Buch ist gerade entliehen an " + bir.getFirstname() + " " + bir.getSurname() + ".<br />";
            }

        } else {
            info += "Der Barcode des Buchs wurde geändert.check_mark<br />";
            info += "Alter Barcode " + cbr.oldBarcode + "<br />";
            info += "Neuer Barcode " + cbr.newBarcode + "<br />";
        }

        BookCopyDAO.changeBarcode(cbr.oldBarcode, cbr.newBarcode, cbr.school_id, con);

        return new ChangeBarcodeResponse("success", info);

    }

    private SortOutResponse sortOutBookCopy(SortOutRequest sor, Connection con) {

        List<BookCopyInfoRecord> bookCopyInfoRecords = BookCopyDAO.getBookCopyInInfo(sor.school_id, sor.barcode, con);

        if(bookCopyInfoRecords.size() > 0){

            if(sor.sort_out_date == null){
                sor.sort_out_date = Calendar.getInstance().getTime();
            }

            BookCopyInfoRecord info = bookCopyInfoRecords.get(0);
            BookCopyDAO.setSortedOutDate(info.getBook_copy_id(), sor.sort_out_date, con);

            String message = "<div>Das Buch mit Barcode " + sor.barcode + " wurde ausgemustert.</div>";
            message += "<div><span style=\"font-weight: bold\">Titel: " + info.getTitle() + "</span></div>";

            if(info.getFirstname() != null && info.getSurname() != null && info.getBorrows_id() != null){
                BorrowsDAO.setReturnDate(info.getBorrows_id(), sor.sort_out_date, con);
                message += "<div style=\"font-weight: bold; color:#800000\"> Das Buch war an " + info.getFirstname() +
                        " " + info.getSurname() + " entliehen. => Die Rückgabe wurde gebucht.</div>";
            }

            message += "<br />";

            return new SortOutResponse("success", message);

        } else {
            return new SortOutResponse("error", "Es wurde kein Buch mit dem Barcode " + sor.barcode + " gefunden.");
        }

    }

    private MergeStudentsResponse mergeStudents(MergeStudentsRequest msr, Connection con) {

        Long good_student_id = msr.student1_id;
        Long student_to_remove_id = msr.student2_id;

        if(msr.deleteStudent12 == 1){
            good_student_id = msr.student2_id;
            student_to_remove_id = msr.student1_id;
        }

        // Move borrowed books from student_to_remove to good_student
        Integer movedBorrowRecords = LibrarySettingsDAO.mergeStudents(good_student_id, student_to_remove_id, con);

        // Remove student_to_remove
        StudentDAO.deleteCascading(Arrays.asList(student_to_remove_id), con);

        String message = "" + movedBorrowRecords + " ausgeliehene Bücher wurden übertragen, danach der Schülerdatensatz " + msr.deleteStudent12 + " gelöscht.check_mark";

        return new MergeStudentsResponse("success", message);
    }

    private GridResponseGet<BorrowerRecord> getStudentList(GridRequestGet getData, Connection con) {

        List<BorrowerRecord> records = LibraryDAO.getBorrowerList(
                getData.getSchool_id(), getData.getSchool_term_id(), con, false, false);

        return new GridResponseGet<BorrowerRecord>(GridResponseStatus.success,
                records.size(), records, "", null);

    }

    private DeleteOldRecordsResponse deleteResignedTeachers(DeleteOldRecordsRequest dobr1, Connection con) {

        List<Teacher> teachers = LibrarySettingsDAO.getResignedTeachers(dobr1.school_id, con);

        TeacherDAO.deleteCascadingByTeacherList(teachers, con);

        String message = "check_mark Folgende " + teachers.size() + " Schülerdatensätze wurden gelöscht: <br />";

        message += teachers.stream().map(teacher -> teacher.getFullName()).collect(Collectors.joining("; "));

        return new DeleteOldRecordsResponse("success", message);

    }

    private DeleteOldRecordsResponse deleteResignedStudents(DeleteOldRecordsRequest dobr1, Connection con) {

        List<Student> students = LibrarySettingsDAO.getResignedStudents(dobr1.school_id, dobr1.date_from, con);

        StudentDAO.deleteCascadingByStudentList(students, con);

        String message = "check_mark Folgende " + students.size() + " Schülerdatensätze wurden gelöscht: <br />";

        message += students.stream().map(student -> student.getFullName()).collect(Collectors.joining("; "));

        return new DeleteOldRecordsResponse("success", message);

    }

    private DeleteOldRecordsResponse deleteOldBookings(DeleteOldRecordsRequest dobr, Connection con) {

        Integer size = LibrarySettingsDAO.deleteOldBookings(dobr.school_id, dobr.date_from, con);

        return new DeleteOldRecordsResponse("success", size.toString() + " Datensätze wurden gelöscht.check_mark");

    }


}
