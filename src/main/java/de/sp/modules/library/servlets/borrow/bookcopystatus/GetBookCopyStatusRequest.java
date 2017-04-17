package de.sp.modules.library.servlets.borrow.bookcopystatus;

public class GetBookCopyStatusRequest {
	
	private String barcode;
	
	private Long school_id;


	public String getBarcode() {
		return barcode;
	}


	public Long getSchool_id() {
		return school_id;
	}

	
}
