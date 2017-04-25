package de.sp.modules.library.servlets.reports.reportsschueler.BarcodeTestsheet;

import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.model.BookCopy;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Martin on 25.04.2017.
 */
public class AvailableBookFinder {

    // Maps book-Ids to Lists of barcodes
    private HashMap<Long, ArrayList<String>> availableBooks = new HashMap<>();

    public AvailableBookFinder(Long school_id, Connection con) {

        List<BookCopy> bookCopies = BookCopyDAO.findAvailableCopiesBySchool(con, school_id);

        for(BookCopy bookCopy : bookCopies) {

            ArrayList<String> barcodeList = availableBooks.get(bookCopy.getBook_id());
            if(barcodeList == null){
                barcodeList = new ArrayList<>();
                availableBooks.put(bookCopy.getBook_id(), barcodeList);
            }

            barcodeList.add(bookCopy.getBarcode());

        }

    }

    public String popBarcode(Long book_id){

        ArrayList<String> barcodeList = availableBooks.get(book_id);

        if(barcodeList == null){
            return "00000000";
        }

        if(barcodeList.size() > 0){

            String barcode = barcodeList.get(0);
            barcodeList.remove(0);

            return barcode;

        } else {

            return "00000000";

        }


    }

}
