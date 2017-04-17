package de.sp.modules.library.servlets.borrow.bookformstore;

@SuppressWarnings("unused")
public class BookFormEntry {

	private Long form_id;
	private String form_name;
	private Long curriculum_id;
	private String curriculum_name;
	private Integer languageyear;
	
	public BookFormEntry(Long form_id, String form_name, Long curriculum_id,
			String curriculum_name, Integer languageyear) {
		super();
		this.form_id = form_id;
		this.form_name = form_name;
		this.curriculum_id = curriculum_id;
		this.curriculum_name = curriculum_name;
		this.languageyear = languageyear;
	}

	
	
}
