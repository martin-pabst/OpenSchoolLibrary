package de.sp.database.model;

public class Bookshop {

    private long id;
    private long school_id;
    private String name;
    private long address_id;
    private boolean active;

    public Bookshop(long id, long school_id, String name, long address_id, boolean active) {
        this.id = id;
        this.school_id = school_id;
        this.name = name;
        this.address_id = address_id;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAddress_id() {
        return address_id;
    }

    public boolean isActive() {
        return active;
    }

    public long getSchool_id() {
        return school_id;
    }
}
