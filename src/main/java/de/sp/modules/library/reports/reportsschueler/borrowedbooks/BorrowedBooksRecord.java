package de.sp.modules.library.reports.reportsschueler.borrowedbooks;

import de.sp.database.model.BookCopyStatus;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 17.04.2017.
 */
public class BorrowedBooksRecord implements Comparable<BorrowedBooksRecord> {

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
    public Long book_copy_id;

    public List<BookCopyStatus> statusList = new ArrayList<>();


    public BorrowedBooksRecord(){

    }

    public String getBeginDateFormatted(){
        if(begindate == null){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        return sdf.format(begindate);
    }

    public JRBeanCollectionDataSource getStatusDataSource(){
        return new JRBeanCollectionDataSource(statusList);
    }

    public List<BookCopyStatus> getStatusList() {
        return statusList;
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
    public int compareTo(BorrowedBooksRecord o) {

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
