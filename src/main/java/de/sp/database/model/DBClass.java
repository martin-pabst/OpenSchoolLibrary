package de.sp.database.model;

public class DBClass {

	private Long id;

	private Long school_term_id;

	private String name;

	private int year_of_school;
	
	private Long form_id;

	public DBClass() {
	}

	public DBClass(Long id, Long school_term_id, String name, int year_of_school, Long form_id) {

		this.id = id;
		this.school_term_id = school_term_id;
		this.name = name;
		this.year_of_school = year_of_school;
		this.form_id = form_id;
	}

	public Long getId() {

		return id;

	}

	public Long getSchool_term_id() {

		return school_term_id;

	}

	public String getName() {

		return name;

	}

	public int getYear_of_school() {

		return year_of_school;

	}

	public void setSchoolTerm(SchoolTerm schoolTerm) {

		school_term_id = schoolTerm.getId();
		
	}

	public Long getForm_id() {
		return form_id;
	}
	
	
	

}