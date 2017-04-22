package de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent;

/**
 * Created by Martin on 22.04.2017.
 */
public class LibraryAddStundentRequest {

    public String cmd;
    public Long recid;

    public Long school_id;
    public Long school_term_id;
    public Long student_id;
    public Long student_school_term_id;

    public LibraryAddStudentRequestRecord record;

    public LibraryAddStundentRequest(){};




}
