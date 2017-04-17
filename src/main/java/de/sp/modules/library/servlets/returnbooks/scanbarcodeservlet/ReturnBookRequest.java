package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

public class ReturnBookRequest {
	
	private String barcode;
	
	private boolean performReturn;
		
	private Long school_id;

	public String getBarcode() {
		return barcode;
	}

	public Long getSchool_id() {
		return school_id;
	}

	public boolean isPerformReturn() {
		return performReturn;
	}
	
	
	
	
}
