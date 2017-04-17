package de.sp.modules.library.servlets.borrow.bookcopystatus;

import de.sp.database.model.BookCopyStatus;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.ReturnBookResponse;

import java.util.List;

/**
 * Created by Martin on 16.04.2017.
 */
public class BookCopyStatusResponse {

    public List<BookCopyStatus> statusList;
    public ReturnBookResponse returnBookResponse;

    public BookCopyStatusResponse(List<BookCopyStatus> statusList, ReturnBookResponse returnBookResponse) {

        this.statusList = statusList;
        this.returnBookResponse = returnBookResponse;

    }
}
