package de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 22.04.2017.
 */
public class LibraryAddStundentRequest extends BaseRequestData{

    public String cmd;

    @Validation(notNull = true)
    public Long recid;

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long school_term_id;

    public Long student_id;
    public Long student_school_term_id;

    public LibraryAddStudentRequestRecord record;

    public LibraryAddStundentRequest(){};




}
