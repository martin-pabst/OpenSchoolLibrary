package de.sp.modules.library.servlets.borrow.bookformstore;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BookFormStoreRecord {

	private Long book_id;
	private String title;
	private String author;
	private String subject;
	private Long subject_id;

	private transient Long book_form_id;
	private transient Long form_id;
	private transient String form_name;
	private transient Long curriculum_id;
	private transient String curriculum_name;
	private transient Integer languageyear;

	private List<BookFormEntry> bookFormEntries = new ArrayList<>();

	public BookFormStoreRecord() {
		
	}

	public void consolidateWith(BookFormStoreRecord other) {
		
		if(other.book_form_id != null){			
			bookFormEntries.add(new BookFormEntry(other.form_id, other.form_name,
					other.curriculum_id, other.curriculum_name, other.languageyear));
		}
		
	}

	public Long getBook_id() {
		return book_id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getSubject() {
		return subject;
	}

	public Long getSubject_id() {
		return subject_id;
	}

	public Long getBook_form_id() {
		return book_form_id;
	}

	public Long getForm_id() {
		return form_id;
	}

	public String getForm_name() {
		return form_name;
	}

	public Long getCurriculum_id() {
		return curriculum_id;
	}

	public String getCurriculum_name() {
		return curriculum_name;
	}

	public Integer getLanguageyear() {
		return languageyear;
	}

	public List<BookFormEntry> getBookFormEntries() {
		return bookFormEntries;
	}
}
