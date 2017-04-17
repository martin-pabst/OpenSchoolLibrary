package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import java.util.Date;
import java.util.List;

import de.sp.database.model.BookCopyStatus;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BarcodeInfoStatus;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BorrowedBookRecord;

@SuppressWarnings("unused")
public class ReturnBookResponse {

	private BarcodeInfoStatus status;
	private String message;

	private Long teacher_id;
	private Long student_id; // only one of both is set

	private String student_firstname;
	private String student_surname;
	private String teacher_firstname;
	private String teacher_surname;

	private boolean isStudent;

	private Long borrows_id;
	private Date begindate;
	private Date enddate;
	private Date return_date;

	private Long book_copy_id;

	private Long book_id;
	private String title;
	private String author;
	private Long subject_id;
	private String subject;

	private List<BorrowedBookRecord> borrowedBooksList;

	private List<BookCopyStatus> statusList; // Statusentries for book with
												// given barcode

	private List<FeeRecord> feeList;

	public ReturnBookResponse(BarcodeInfoStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public void setBorrowedBooksList(List<BorrowedBookRecord> borrowedBooksList) {
		this.borrowedBooksList = borrowedBooksList;
	}

	public void setFeeList(List<FeeRecord> feeList) {
		this.feeList = feeList;
	}

	public void setStatusList(List<BookCopyStatus> statusList) {
		this.statusList = statusList;
	}

	public boolean isStudent() {
		return student_id != null;
	}

	public Long getTeacher_id() {
		return teacher_id;
	}

	public Long getStudent_id() {
		return student_id;
	}

	public Long getBookCopyId() {
		return book_copy_id;
	}

	public Long getBorrows_id() {
		return borrows_id;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setStatus(BarcodeInfoStatus status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BorrowedBookRecord> getBorrowedBooksList() {
		return borrowedBooksList;
	}

	
}
