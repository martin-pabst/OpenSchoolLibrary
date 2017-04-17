package de.sp.modules.library.servlets.borrow.bookcopystatus;

import java.util.Date;

public class SaveBookCopyStatusRequest {
	
	private String evidence;
	private String event;
	private String mark;
	private String borrowername;
	private String username;
	private Long book_copy_id;
	private Date statusdate;
	
	private Long school_id;
	
	public SaveBookCopyStatusRequest(String evidence, String event,
			String mark, String borrowername, String username,
			Long book_copy_id, Date statusDate) {
		super();
		this.evidence = evidence;
		this.event = event;
		this.mark = mark;
		this.borrowername = borrowername;
		this.username = username;
		this.book_copy_id = book_copy_id;
		this.statusdate = statusDate;
	}
	
	public String getEvidence() {
		return evidence;
	}
	public String getEvent() {
		return event;
	}
	public String getMark() {
		return mark;
	}
	public String getBorrowername() {
		return borrowername;
	}
	public String getUsername() {
		return username;
	}
	public Long getBook_copy_id() {
		return book_copy_id;
	}
	public Date getStatusDate() {
		return statusdate;
	}

	public Date getStatusdate() {
		return statusdate;
	}

	public Long getSchool_id() {
		return school_id;
	}

	
	
}
