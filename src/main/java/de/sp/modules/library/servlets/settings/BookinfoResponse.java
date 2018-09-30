package de.sp.modules.library.servlets.settings;

import de.sp.database.daos.basic.helper.BookCopyHelperDAO;
import de.sp.database.daos.basic.helper.BookinfoData;
import org.sql2o.Connection;

import java.util.List;

public class BookinfoResponse {

    private List<BookinfoData> bookInfoList;

    private String title = "";
    private String author = "";
    private String publisher = "";
    private String isbn = "";

    private String status = "";
    private String message = "";

    public BookinfoResponse(BookinfoRequest bir, Connection con) {

        BookinfoData lastData = null;
        long lastBorrowsId = -1;

        bookInfoList = BookCopyHelperDAO.getBookinfo(con, bir.barcode, bir.school_id);

        if(bookInfoList.size() == 0){
            status = "error";
            message = "Kein Buch zum Barcode " + bir.barcode + " gefunden.";
            return;
        }

        for(int i = 0; i < bookInfoList.size(); i++){

            BookinfoData bid = bookInfoList.get(i);

            if(bid.getClass_name() != null) {
                bid.setClass_name(bid.getClass_name() + "(" + bid.getSchool_term_name() + ")");

                if (bid.getBorrows_id() == lastBorrowsId && lastData != null && lastData.getClass_name() != null) {
                    lastData.setClass_name(lastData.getClass_name() + ", " + bid.getClass_name());
                    bookInfoList.remove(i);
                    i--;
                } else {
                    lastData = bid;
                    lastBorrowsId = bid.getBorrows_id();
                }
            }

            if(title.isEmpty() && bid.getTitle() != null){
                title = bid.getTitle();
                author = bid.getAuthor();
                isbn = bid.getIsbn();
                publisher = bid.getPublisher();
            }

        }

        status = "success";
        message = "Buch mit Barcode " + bir.barcode + " gefunden.";

    }
}
