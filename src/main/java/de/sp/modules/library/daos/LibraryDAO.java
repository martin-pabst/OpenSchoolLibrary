package de.sp.modules.library.daos;

import de.sp.database.daos.basic.BookCopyStatusDAO;
import de.sp.database.daos.basic.FeeDAO;
import de.sp.database.statements.StatementStore;
import de.sp.modules.library.servlets.borrow.bookformstore.BookFormStoreRecord;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BarcodeInfoRecord;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BorrowedBookRecord;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.modules.library.servlets.reports.ClassRecord;
import de.sp.modules.library.servlets.returnbooks.returnerlist.ReturnerRecord;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.ReturnBookResponse;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryDAO {

    public static List<BorrowerRecord> getBorrowerList(Long school_id, Long school_term_id,
                                                       Connection con, boolean withTeachers) {

		/*
         * Students
		 */

        String sql1 = StatementStore
                .getStatement("library.getBorrowerStudentList");

        List<BorrowerRecord> studentBorrowersRaw = con.createQuery(sql1)
                .addParameter("school_id", school_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(BorrowerRecord.class);

        ArrayList<BorrowerRecord> borrowersConsolidated = new ArrayList<>();

        Map<Long, BorrowerRecord> studentMap = new HashMap<>();

        for (BorrowerRecord br : studentBorrowersRaw) {

            BorrowerRecord old = studentMap.get(br.getStudent_id());

            if (old == null) {
                borrowersConsolidated.add(br);
                studentMap.put(br.getStudent_id(), br);
            } else {
                old.consolidateWith(br);
            }

        }

        borrowersConsolidated.forEach(bc -> bc.initStudent());

        if (withTeachers) {
		/*
		 * Teachers
		 */

            String sql2 = StatementStore
                    .getStatement("library.getBorrowerTeacherList");

            List<BorrowerRecord> teacherBorrowers = con.createQuery(sql2)
                    .addParameter("school_id", school_id)
                    .executeAndFetch(BorrowerRecord.class);

            teacherBorrowers.forEach(bc -> bc.initTeacher());

            borrowersConsolidated.addAll(teacherBorrowers);
        }


        return borrowersConsolidated;

    }

    public static BorrowerRecord getBorrower(Long school_id, Long school_term_id, Long student_id,
                                                       Connection con) {

        String sql1 = StatementStore
                .getStatement("library.getBorrowerStudentList");

        sql1 += " and student.id = :student_id";


        List<BorrowerRecord> studentBorrowersRaw = con.createQuery(sql1)
                .addParameter("school_id", school_id)
                .addParameter("school_term_id", school_term_id)
                .addParameter("student_id", student_id)
                .executeAndFetch(BorrowerRecord.class);

        ArrayList<BorrowerRecord> borrowersConsolidated = new ArrayList<>();

        Map<Long, BorrowerRecord> studentMap = new HashMap<>();

        for (BorrowerRecord br : studentBorrowersRaw) {

            BorrowerRecord old = studentMap.get(br.getStudent_id());

            if (old == null) {
                borrowersConsolidated.add(br);
                studentMap.put(br.getStudent_id(), br);
            } else {
                old.consolidateWith(br);
            }

        }

        borrowersConsolidated.forEach(bc -> bc.initStudent());

        return borrowersConsolidated.get(0);

    }


    public static List<BookFormStoreRecord> getBookFormStore(Long school_id,
                                                             Connection con) {

        String sql1 = StatementStore.getStatement("library.getBookFormStore");

        List<BookFormStoreRecord> bookFormStore = con.createQuery(sql1)
                .addParameter("school_id", school_id)
                .executeAndFetch(BookFormStoreRecord.class);

        ArrayList<BookFormStoreRecord> bookFormStoreConsolidated = new ArrayList<>();

        Map<Long, BookFormStoreRecord> bookMap = new HashMap<>();

        for (BookFormStoreRecord br : bookFormStore) {

            BookFormStoreRecord old = bookMap.get(br.getBook_id());

            if (old == null) {
                bookFormStoreConsolidated.add(br);
                bookMap.put(br.getBook_id(), br);
                br.consolidateWith(br);
            } else {
                old.consolidateWith(br);
            }

        }

        return bookFormStoreConsolidated;

    }

    public static List<BorrowedBookRecord> getBorrowedBooksForStudent(
            Long student_id, Connection con) {

        String sql = StatementStore
                .getStatement("library.getBorrowedBooksForStudent");

        List<BorrowedBookRecord> list = con.createQuery(sql)
                .addParameter("student_id", student_id)
                .executeAndFetch(BorrowedBookRecord.class);

        return list;

    }

    public static List<BorrowedBookRecord> getBorrowedBooksForTeacher(
            Long teacher_id, Connection con) {

        String sql = StatementStore
                .getStatement("library.getBorrowedBooksForTeacher");

        List<BorrowedBookRecord> list = con.createQuery(sql)
                .addParameter("teacher_id", teacher_id)
                .executeAndFetch(BorrowedBookRecord.class);

        return list;
    }

    public static BarcodeInfoRecord getBarcodeInfo(String barcode,
                                                   Long school_id, Connection con) {

        String sql = StatementStore.getStatement("library.getBarcodeInfo");

        BarcodeInfoRecord bir = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .addParameter("barcode", barcode)
                .executeAndFetchFirst(BarcodeInfoRecord.class);

        if (bir == null) {
            return bir;
        }

        if (bir.getStudent_id() != null) {
            String sql1 = StatementStore
                    .getStatement("library.getLastClassname");

            String class_name = con.createQuery(sql1)
                    .addParameter("student_id", bir.getStudent_id())
                    .executeAndFetchFirst(String.class);

            if (class_name != null) {
                bir.setClass_name(class_name);
            }

        }

        return bir;

    }

    public static List<ReturnerRecord> getReturnerList(Long school_id,
                                                       Long school_term_id, Connection con) {

        String sql1 = StatementStore
                .getStatement("library.getReturnerStudentList");

        List<ReturnerRecord> studentList = con.createQuery(sql1)
                .addParameter("school_id", school_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(ReturnerRecord.class);

        studentList.forEach(record -> record.initStudent());

        String sql2 = StatementStore
                .getStatement("library.getReturnerTeacherList");

        List<ReturnerRecord> teacherList = con.createQuery(sql2)
                .addParameter("school_id", school_id)
                .executeAndFetch(ReturnerRecord.class);

        teacherList.forEach(record -> record.initTeacher());

        studentList.addAll(teacherList);

        return studentList;
    }

    public static ReturnBookResponse getReturnBookResponse(String barcode,
                                                           Long school_id, Connection con) {


        String sql = StatementStore.getStatement("library.getBarcodeInfo");

        ReturnBookResponse rbr = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .addParameter("barcode", barcode)
                .executeAndFetchFirst(ReturnBookResponse.class);

        if (rbr == null) {
            return rbr;
        }


        if (rbr.isStudent()) {
            rbr.setBorrowedBooksList(getBorrowedBooksForStudent(rbr.getStudent_id(), con));
            rbr.setFeeList(FeeDAO.findByStudentId(rbr.getStudent_id(), true, con));
        } else {
            rbr.setBorrowedBooksList(getBorrowedBooksForTeacher(rbr.getTeacher_id(), con));
            rbr.setFeeList(FeeDAO.findByTeacherId(rbr.getTeacher_id(), true, con));
        }

        rbr.setStatusList(BookCopyStatusDAO.findById(rbr.getBookCopyId(), con));


        return rbr;

    }

    public static List<ClassRecord> getClassList(Long school_term_id, Connection con) {

        String sql = StatementStore.getStatement("library.getClassList");

        return con.createQuery(sql).addParameter("school_term_id", school_term_id)
                .executeAndFetch(ClassRecord.class);

    }


}
