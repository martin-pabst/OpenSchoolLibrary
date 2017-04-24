package de.sp.modules.library.servlets.reports.reportsschueler.neededbooks;

/**
 * Created by Martin on 24.04.2017.
 */
public class NeededBookRecord implements Comparable<NeededBookRecord> {

    private String classname;
    private String studentname;
    private String Subject;
    private String book;
    private Long book_id;

    public NeededBookRecord(String classname, String studentname, String subject, String book, Long book_id) {
        this.classname = classname;
        this.studentname = studentname;
        Subject = subject;
        this.book = book;
        this.book_id = book_id;
    }

    public String getClassname() {
        return classname;
    }

    public String getStudentname() {
        return studentname;
    }

    public String getSubject() {
        return Subject;
    }

    public String getBook() {
        return book;
    }

    public Long getBook_id() {
        return book_id;
    }

    @Override
    public int compareTo(NeededBookRecord nbr) {
        if(classname != null && !classname.equals(nbr.classname)){
            return classname.compareTo(nbr.classname);
        }

        if(studentname != null && !studentname.equals(nbr.studentname)){
            return studentname.compareTo(nbr.studentname);
        }

        if(book != null){
            return book.compareTo(nbr.book);
        }

        return 0;
    }
}


