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

}
