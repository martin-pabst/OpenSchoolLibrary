package de.sp.modules.library.servlets.inventory.books;

public class BookFormExtended {

	private Long id;
	
	private String form_name;
	
	private Long form_id;
	
	private String curriculum_name;
	
	private Long curriculum_id;
	
	private Integer languageyear;

	public BookFormExtended(Long id, String form_name, Long form_id,
			String curriculum_name, Long curriculum_id, Integer languageyear) {
		super();
		this.id = id;
		this.form_name = form_name;
		this.form_id = form_id;
		this.curriculum_name = curriculum_name;
		this.curriculum_id = curriculum_id;
		this.languageyear = languageyear;
		
	}

	public Long getId() {
		return id;
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

	
	
}
