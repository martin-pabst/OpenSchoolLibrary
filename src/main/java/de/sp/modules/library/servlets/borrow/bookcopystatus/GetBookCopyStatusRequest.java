package de.sp.modules.library.servlets.borrow.bookcopystatus;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

public class GetBookCopyStatusRequest extends BaseRequestData{

	@Validation(notEmpty = true)
	private String barcode;

	@Validation(notNull = true)
	private Long school_id;


	public String getBarcode() {
		return barcode;
	}


	public Long getSchool_id() {
		return school_id;
	}

	
}
