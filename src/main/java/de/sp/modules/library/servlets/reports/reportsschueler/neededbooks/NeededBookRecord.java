package de.sp.modules.library.servlets.reports.reportsschueler.neededbooks;

import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;

/**
 * Created by Martin on 24.04.2017.
 */
public class NeededBookRecord implements Comparable<NeededBookRecord> {

    private BorrowerRecord borrower;
    private String class_name;
    private Long class_id;
    private String studentname;
    private Long student_id;
    private String subject;
    private String book;
    private Long book_id;

    public NeededBookRecord(BorrowerRecord borrower, String classname,Long class_id, String studentname, Long student_id,
                            String subject, String book, Long book_id) {

        this.borrower = borrower;
        this.class_name = classname;
        this.class_id = class_id;
        this.studentname = studentname;
        this.student_id = student_id;
        this.subject = subject;
        this.book = book;
        this.book_id = book_id;

    }

    public String getClass_name() {
        return class_name;
    }

    public Long getClass_id() {
        return class_id;
    }

    public String getStudentname() {
        return studentname;
    }

    public Long getStudent_id(){ return student_id; }

    public String getSubject() {
        return subject;
    }

    public String getBook() {
        return book;
    }

    public Long getBook_id() {
        return book_id;
    }

    public BorrowerRecord getBorrower() {
        return borrower;
    }

    @Override
    public int compareTo(NeededBookRecord nbr) {
        
        
        if (class_name != null && nbr.class_name != null && !class_name.equals(nbr.class_name)) {
            return class_name.compareTo(nbr.class_name);
        }

        if(class_name != null && nbr.class_name == null){
            return 1;
        }
        
        if(class_name == null && nbr.class_name != null){
            return -1;
        }

        
        
        if (studentname != null && nbr.studentname != null && !studentname.equals(nbr.studentname)) {
            return studentname.compareTo(nbr.studentname);
        }

        if (book != null && nbr.book != null) {
            return book.compareTo(nbr.book);
        }

        return 0;
    }

    public String getLanguagesReligionCurriculum(){
        if(borrower != null){
            String s = "(";

            if(borrower.getLanguages() != null){
                s += borrower.getLanguages() + ", ";
            }

            if(borrower.getReligion() != null){
                s += borrower.getReligion() + ", ";
            }

            if(borrower.getCurriculum_name() != null){
                s += borrower.getCurriculum_name() + ", ";
            }

            if(s.endsWith(", ")){
                s = s.substring(0, s.length() - 2);
            }

            s += ")";

            return s;
        } else {
            return "";
        }
    }

    public String getStudentNameNew(){
        
        String name = borrower.getSurname();
        if(borrower.getBefore_surname() != null && !borrower.getBefore_surname().isEmpty()){
            name = borrower.getBefore_surname() + " " + name;
        }
        if(borrower.getAfter_surname() != null && !borrower.getAfter_surname().isEmpty()){
            name = name + " " + borrower.getAfter_surname();
        }

        name += ", " + borrower.getFirstname();
        
        return name;
    }

}


