package de.sp.modules.library.servlets.settings;

import de.sp.database.daos.basic.helper.BookCopyHelperDAO;
import de.sp.database.daos.basic.helper.BookinfoData;
import org.sql2o.Connection;

import java.util.List;

public class BookinfoResponse {

    private List<BookinfoData> bookInfoList;

    private String title;
    private String author;
    private String publisher;
    private String isbn;

    public BookinfoResponse(BookinfoRequest bir, Connection con) {

        BookinfoData lastData = null;
        long lastBorrowsId = -1;

        bookInfoList = BookCopyHelperDAO.getBookinfo(con, bir.barcode, bir.school_id);

        for(int i = 0; i < bookInfoList.size(); i++){

            BookinfoData bid = bookInfoList.get(i);

            if(bid.getClass_name() != null) {
                bid.setClass_name(bid.getClass_name() + "(" + bid.getSchool_term_name() + ")");

                if (bid.getBorrows_id() == lastBorrowsId && lastData.getClass_name() != null) {
                    lastData.setClass_name(lastData.getClass_name() + bid.getClass_name());
                }
                bookInfoList.remove(i);
                i--;
            }

            if(title == null && bid.getTitle() != null){
                title = bid.getTitle();
                author = bid.getAuthor();
                isbn = bid.getIsbn();
                publisher = bid.getPublisher();
            }

        }

    }
}
