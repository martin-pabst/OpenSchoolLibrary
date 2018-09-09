package de.sp.modules.library.servlets.orderbooks;

import java.util.List;

public class OrderBooksRequest {

    private long school_id;
    private List<OrderBookRow> selectedRows;
    private String address;

    public List<OrderBookRow> getSelectedRows() {
        return selectedRows;
    }

    public String getAddress() {
        return address;
    }

    public long getSchool_id() {
        return school_id;
    }
}
