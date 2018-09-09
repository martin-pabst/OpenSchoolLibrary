package de.sp.database.model;

public class Bookorderposition {

    private long id;
    private long bookorder_id;
    private long book_id;
    private int number;

    public Bookorderposition(long id, long bookorder_id, long book_id, int number) {
        this.id = id;
        this.bookorder_id = bookorder_id;
        this.book_id = book_id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public long getBookorder_id() {
        return bookorder_id;
    }

    public long getBook_id() {
        return book_id;
    }

    public int getNumber() {
        return number;
    }
}
