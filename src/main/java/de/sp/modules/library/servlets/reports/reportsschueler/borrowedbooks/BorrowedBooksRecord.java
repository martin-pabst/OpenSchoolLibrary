package de.sp.modules.library.servlets.reports.reportsschueler.borrowedbooks;

import java.util.Date;

/**
 * Created by Martin on 17.04.2017.
 */
public class BorrowedBooksRecord {

    public String class_name;
    public String firstname;
    public String surname;
    public Date exit_date;
    public String title;
    public String begindate;

    public String getClass_name() {
        return class_name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public Date getExit_date() {
        return exit_date;
    }

    public String getTitle() {
        return title;
    }

    public String getBegindate() {
        return begindate;
    }

    /*
    class.name AS class_name,
    student.firstname,
    student.surname,
    student.exit_date,
    book.title,
    borrows.begindate
*/


}
