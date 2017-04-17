package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

public class FeeGetRequest {
	
	private Long school_id;
	
	private Long student_id;
	private Long teacher_id;
	private boolean onlyUnpaidFees;
	
	
	public Long getStudent_id() {
		return student_id;
	}
	public Long getTeacher_id() {
		return teacher_id;
	}
	public boolean isOnlyUnpaidFees() {
		return onlyUnpaidFees;
	}
	public Long getSchool_id() {
		return school_id;
	}
	
	
	
}
