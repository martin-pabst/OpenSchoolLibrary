package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

public class FeeDeleteRequest extends BaseRequestData {

	@Validation(notNull = true)
	private Long school_id;

	@Validation(notNull = true)
	private Long fee_id;

	public Long getFee_id(){
		return fee_id;
	}
	
	public Long getSchool_id() {
		return school_id;
	}
	
	
	
}
