package de.sp.database.daos.basic.helper;

import java.util.Date;

public class BookinfoData implements Comparable<BookinfoData> {

    // Borrow data
    private Long borrows_id;
    private transient String title;
    private transient String author;
    private transient String publisher;
    private transient String isbn;
    private Long student_id;
    private String student_surname;
    private String student_firstname;
    private String school_term_name;
    private String class_name;
    private Long teacher_id;
    private String teacher_surname;
    private String teacher_firstname;
    private Date begindate;
    private Date return_date;

    //book copy status
    private Date statusdate;
    private String evidence;
    private String username;
    private String borrowername;
    private String mark;

    @Override
    public int compareTo(BookinfoData d) {
        Date d1 = begindate;
        if(d1 == null){
            d1 = statusdate;
        }

        Date d2 = d.begindate;
        if(d2 == null){
            d2 = d.statusdate;
        }

        if(d1 == null){
            return -1;
        }

        if(d2 == null){
            return 1;
        }

        if(d1.equals(d2)){

            if(borrows_id == null){
                return -1;
            }

            if(d.borrows_id == null){
                return 1;
            }

            return borrows_id.compareTo(d.borrows_id);

        }

        return d1.compareTo(d2);
    }

    public Long getBorrows_id() {
        return borrows_id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public String getStudent_surname() {
        return student_surname;
    }

    public String getStudent_firstname() {
        return student_firstname;
    }

    public String getSchool_term_name() {
        return school_term_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public Long getTeacher_id() {
        return teacher_id;
    }

    public String getTeacher_surname() {
        return teacher_surname;
    }

    public String getTeacher_firstname() {
        return teacher_firstname;
    }

    public Date getBegindate() {
        return begindate;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public Date getStatusdate() {
        return statusdate;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getUsername() {
        return username;
    }

    public String getBorrowername() {
        return borrowername;
    }

    public String getMark() {
        return mark;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
