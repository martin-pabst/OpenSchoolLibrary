package de.sp.database.model;

import java.util.Date;

public class Bookorder {

    private long id;
    private long school_id;
    private long bookshop_id;
    private Date orderdate;

    public Bookorder(long id, long school_id, long bookshop_id, Date orderdate) {
        this.id = id;
        this.school_id = school_id;
        this.bookshop_id = bookshop_id;
        this.orderdate = orderdate;
    }

    public long getId() {
        return id;
    }

    public long getSchool_id() {
        return school_id;
    }

    public long getBookshop_id() {
        return bookshop_id;
    }

    public Date getOrderdate() {
        return orderdate;
    }
}
