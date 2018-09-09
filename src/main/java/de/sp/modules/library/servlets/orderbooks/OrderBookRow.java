package de.sp.modules.library.servlets.orderbooks;

import de.sp.database.model.Book;

public class OrderBookRow {

    private long id;
    private long number;
    private Book book;

    public long getId() {
        return id;
    }

    public long getNumber() {
        return number;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {

        this.book = book;

    }
}
