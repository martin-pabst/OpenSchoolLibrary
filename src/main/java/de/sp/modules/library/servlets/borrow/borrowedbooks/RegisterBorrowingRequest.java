package de.sp.modules.library.servlets.borrow.borrowedbooks;

public class RegisterBorrowingRequest {
	
	private String barcode;
	
	private boolean unbookFromPreviousBorrower;
	
	private Long student_id;
	
	private Long teacher_id;
	
	private Long school_id;

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
	
	
	
}
