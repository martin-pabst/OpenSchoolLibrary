package de.sp.modules.library.servlets.inventory.copies;

import java.util.Date;

public class BookCopyInfoRecord {
	private Long book_id;
	private Long book_copy_id;
	private String title;
	private String author;
	private String barcode;
	private Date purchase_date;

	// only set if book copy is borrowed and not returned:
	private String firstname;
	private String surname;
	private Long borrows_id;

	public Long getBook_id() {
		return book_id;
	}
	public Long getBook_copy_id() {
		return book_copy_id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getSurname() {
		return surname;
	}

	public Long getBorrows_id() {
		return borrows_id;
	}

	public String getTitle() {
		
		if(title == null){
			return "";
		}
		
		return title;
	}
	
	public String getBarcode() {
		return barcode;
	}
	public String getAuthor() {
		
		if(author == null){
			return "";
		}
		
		return author;
	}

	public Date getPurchase_date() {
		return purchase_date;
	}
}
