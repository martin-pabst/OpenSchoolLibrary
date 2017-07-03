package de.sp.modules.library.servlets.borrow.borrowedbooks;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

public class RegisterBorrowingRequest extends BaseRequestData{

	@Validation(notNull = true)
	private String barcode;
	
	private boolean unbookFromPreviousBorrower;

	private Long student_id;
	
	private Long teacher_id;

	@Validation(notNull = true)
	private Long school_id;

	private Boolean over_holidays;

	public String getBarcode() {
		return barcode;
	}

	public boolean isUnbookFromPreviousBorrower() {
		return unbookFromPreviousBorrower;
	}

	public Long getStudent_id() {
		return student_id;
	}

	public Long getTeacher_id() {
		return teacher_id;
	}

	public Long getSchool_id() {
		return school_id;
	}

	public Boolean getOver_holidays() {
		return over_holidays;
	}

	public void normalizeBarcode(){
		while(barcode.startsWith("0")){
			barcode = barcode.substring(1);
		}
	}

}
