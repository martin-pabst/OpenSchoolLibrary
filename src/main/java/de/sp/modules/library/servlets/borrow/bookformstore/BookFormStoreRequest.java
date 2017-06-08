package de.sp.modules.library.servlets.borrow.bookformstore;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

public class BookFormStoreRequest extends BaseRequestData {

	@Validation(notNull = true)
	private Long school_id;

	public Long getSchool_id() {
		return school_id;
	}
	
	
	
}
