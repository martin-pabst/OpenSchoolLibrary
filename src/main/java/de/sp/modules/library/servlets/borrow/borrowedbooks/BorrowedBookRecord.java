package de.sp.modules.library.servlets.borrow.borrowedbooks;

import java.util.Date;

@SuppressWarnings("unused")
public class BorrowedBookRecord {

	private Long id; // book_copy.id
	private Long borrows_id;
	private String title;
	private String author;
	private Long book_id;
	private String subject;
	private String barcode;
	private Date begindate;
	private Date enddate;
	private Date return_date;
	private Double amount; // Fee.amount
	private Boolean over_holidays;
	
	public String getBarcode() {
		return barcode;
	}

	public Long getBook_id() {
		return book_id;
	}
}
