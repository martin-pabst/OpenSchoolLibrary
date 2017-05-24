package de.sp.modules.library.reports.reportsother.borrowoverholidays;

import java.util.Date;

/**
 * Created by Martin on 17.04.2017.
 */
public class BorrowedOverHolidaysRecord implements Comparable<BorrowedOverHolidaysRecord> {

    public Long class_id;
    public Long student_id;
    public String class_name;
    public String firstname;
    public String surname;
    public Date exit_date;
    public String title;
    public Long book_id;
    public Date begindate;
    public String barcode;

    public BorrowedOverHolidaysRecord(){

    }

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

    public Date getBegindate() {
        return begindate;
    }

    @Override
    public int compareTo(BorrowedOverHolidaysRecord o) {

        if(class_name != null && o.class_name != null){
            if(!class_name.equals(o.class_name)){
                return class_name.compareTo(o.class_name);
            }
        }

        if(surname != null && o.surname != null){
            if(!surname.equals(o.surname)){
                return surname.compareTo(o.surname);
            }
        }

        if(firstname != null && o.firstname != null){
            if(!firstname.equals(o.firstname)){
                return firstname.compareTo(o.firstname);
            }
        }

        if(title != null && o.title != null){
            return title.compareTo(o.title);
        }

        return 0;
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
