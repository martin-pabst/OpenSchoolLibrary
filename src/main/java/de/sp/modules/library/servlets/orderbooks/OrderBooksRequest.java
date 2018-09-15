package de.sp.modules.library.servlets.orderbooks;

import java.util.List;

public class OrderBooksRequest {

    private long school_id;
    private List<OrderBookRow> selectedRows;
    private String addressLeft;
    private String addressRight;
    private String orderId;
    private String customerId;
    private double rabatt;
    private String librarysettings;

    public List<OrderBookRow> getSelectedRows() {
        return selectedRows;
    }

    public long getSchool_id() {
        return school_id;
    }

    public String getAddressLeft() {
        return addressLeft;
    }

    public String getAddressRight() {
        return addressRight;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getLibrarysettings() {
        return librarysettings;
    }

    public double getRabatt() {
        return rabatt;
    }
}
