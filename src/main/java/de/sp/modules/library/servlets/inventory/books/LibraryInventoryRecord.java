package de.sp.modules.library.servlets.inventory.books;

import java.util.ArrayList;
import java.util.List;

public class LibraryInventoryRecord {

	private Long id;

	private String title;

	private String author;

	private String publisher;

	private String isbn;

	private String remarks;

	private String approval_code;

	private String edition;

	private Double price;

	private String subject_name;

	private Long subject_id;

	private Long book_form_id;

	private String form_name;

	private Long form_id;

	private String curriculum_name;

	private Long curriculum_id;

	private Integer languageyear;

	private LibraryInventorySubjectData subject;

	private List<BookFormExtended> bookFormList = new ArrayList<>();

	public LibraryInventoryRecord() {

	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getApproval_code() { return approval_code; }

	public String getEdition() { return edition; }

	public Double getPrice() {
		return price;
	}

	public void addBookFormExtended(LibraryInventoryRecord lir) {
		if (lir.book_form_id != null) {
			bookFormList.add(new BookFormExtended(lir.book_form_id,
					lir.form_name, lir.form_id, lir.curriculum_name,
					lir.curriculum_id, lir.languageyear));
		}

	}

	public void initSubjectData() {
		subject = new LibraryInventorySubjectData(subject_id, subject_name);
	}

	public String getSubject_name() {
		return subject_name;
	}

	public Long getSubject_id() {
		return subject_id;
	}

	public Long getBook_form_id() {
		return book_form_id;
	}

	public String getForm_name() {
		return form_name;
	}

	public Long getForm_id() {
		return form_id;
	}

	public String getCurriculum_name() {
		return curriculum_name;
	}

	public Long getCurriculum_id() {
		return curriculum_id;
	}

	public Integer getLanguageyear() {
		return languageyear;
	}

	public LibraryInventorySubjectData getSubject() {
		return subject;
	}

	public List<BookFormExtended> getBookFormList() {
		return bookFormList;
	}

}
