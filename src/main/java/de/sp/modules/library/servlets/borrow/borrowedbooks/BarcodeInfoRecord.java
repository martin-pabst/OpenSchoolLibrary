package de.sp.modules.library.servlets.borrow.borrowedbooks;

import java.util.Date;

@SuppressWarnings("unused")
public class BarcodeInfoRecord {

	private BarcodeInfoStatus status;
	private String message = "";

	public BarcodeInfoRecord(BarcodeInfoStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	private Long book_copy_id;

	private Long book_id;
	private String title;
	private String author;
	private Long subject_id;
	private String subject;

	private Long borrows_id;
	private Date begindate;
	private Date enddate;
	private Date return_date;

	private Long student_id;
	private String student_firstname;
	private String student_surname;
	private String class_name = "";

	private Long teacher_id;
	private String teacher_firstname;
	private String teacher_surname;

	public Long getStudent_id() {
		return student_id;
	}

	public Long getTeacher_id() {
		return teacher_id;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public void setBorrows_id(Long borrows_id) {
		this.borrows_id = borrows_id;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public void setStatus(BarcodeInfoStatus status) {
		this.status = status;
	}

	public BarcodeInfoStatus getStatus() {
		return status;
	}

	public boolean bookIsAlreadyBorrowed() {
		return student_id != null || teacher_id != null;
	}

	public void setStatusAlreadyBorrowed() {

		if (student_id != null) {
			status = BarcodeInfoStatus.studentHasBook;
		}

		if (teacher_id != null) {
			status = BarcodeInfoStatus.teacherHasBook;
		}

	}

	public Long getBorrows_id() {
		return borrows_id;
	}

	public Long getBook_Copy_id() {
		return book_copy_id;
	}

	public Date getReturn_date() {
		return return_date;
	}

}
