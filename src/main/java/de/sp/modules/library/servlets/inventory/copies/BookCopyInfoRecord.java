package de.sp.modules.library.servlets.inventory.copies;

public class BookCopyInfoRecord {
	private Long book_id;
	private Long book_copy_id;
	private String title;
	private String author;
	private String barcode;
	public Long getBook_id() {
		return book_id;
	}
	public Long getBook_copy_id() {
		return book_copy_id;
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
	
	
}
