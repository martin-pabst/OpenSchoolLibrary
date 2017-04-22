package de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent;

import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;

/**
 * Created by Martin on 22.04.2017.
 */
public class LibraryAddStudentResponse {

    public BorrowerRecord borrowerRecord;

    public String status;

    public String message;

    public LibraryAddStudentResponse(BorrowerRecord borrowerRecord, String status, String message) {
        this.borrowerRecord = borrowerRecord;
        this.status = status;
        this.message = message;
    }
}
