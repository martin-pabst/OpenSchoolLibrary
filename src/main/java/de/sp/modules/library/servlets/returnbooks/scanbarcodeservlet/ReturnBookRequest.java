package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

public class ReturnBookRequest extends BaseRequestData{

	@Validation(notEmpty = true)
	private String barcode;
	
	private boolean performReturn;

	@Validation(notNull = true)
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
