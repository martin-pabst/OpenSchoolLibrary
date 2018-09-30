package de.sp.database.daos.basic.helper;

import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookCopyHelperDAO {

    public static List<BookinfoData> getBookinfo(Connection con, String barcode, long school_id) {

        String sql1 = StatementStore.getStatement("book_copy.bookinfo1");

        List<BookinfoData> borrowList = con.createQuery(sql1)
                .addParameter("school_id", school_id)
                .addParameter("barcode", barcode)
                .executeAndFetch(BookinfoData.class);

        String sql2 = StatementStore.getStatement("book_copy.bookinfo2");

        List<BookinfoData> statusList = con.createQuery(sql2)
                .addParameter("school_id", school_id)
                .addParameter("barcode", barcode)
                .executeAndFetch(BookinfoData.class);

        ArrayList<BookinfoData> list = new ArrayList<>();
        list.addAll(borrowList);
        list.addAll(statusList);

        Collections.sort(list);


        return list;

    }




}
