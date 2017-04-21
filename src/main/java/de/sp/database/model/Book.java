package de.sp.database.model;

public class Book {

	private Long id;

	private Long school_id;

	private String title;

	private String author;

	private String isbn;

	private String publisher;

	private String remarks;

	private String approval_code;

	private String edition;

	private Long subject_id;
	
	private Double price;



	public Book() {
	}

	public Book(Long id, Long school_id, String title, String author,
			String isbn, String publisher, String remarks, String approval_code, String edition, Long subject_id, Double price) {
		super();
		this.id = id;
		this.school_id = school_id;
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.publisher = publisher;
		this.remarks = remarks;
		this.subject_id = subject_id;
		this.price = price;
		this.approval_code = approval_code;
		this.edition = edition;
	}

	public Long getId() {

		return id;

	}

	public Long getSchool_id() {

		return school_id;

	}

	public String getTitle() {

		return title;

	}

	public String getIsbn() {

		return isbn;

	}

	public String getPublisher() {

		return publisher;

	}

	public String getRemarks() {

		return remarks;

	}

	public String getAuthor() {
		return author;
	}

	public Long getSubject_id() {
		return subject_id;
	}

	public Double getPrice() {
		return price;
	}

	public String getApproval_code() {
		return approval_code;
	}

	public String getEdition() {
		return edition;
	}
}