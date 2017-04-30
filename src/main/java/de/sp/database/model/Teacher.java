package de.sp.database.model;

public class Teacher {

	private Long id;

	private Long school_id;

	private Long user_id;

	private String surname;

	private String firstname;

	private String before_surname;

	private String after_surname;

	private String abbreviation;
	
	private String external_id;

	private String grade;

	private boolean is_synchronized;

	public Teacher() {
	}


	public Teacher(Long id, Long school_id, Long user_id, String surname,
			String firstname, String before_surname, String after_surname,
			String abbreviation, String external_id, String grade, boolean is_synchronized) {
		super();
		this.id = id;
		this.school_id = school_id;
		this.user_id = user_id;
		this.surname = surname;
		this.firstname = firstname;
		this.before_surname = before_surname;
		this.after_surname = after_surname;
		this.abbreviation = abbreviation;
		this.external_id = external_id;
		this.grade = grade;
		this.is_synchronized = is_synchronized;
	}


	public Long getId() {

		return id;

	}

	public Long getSchool_id() {

		return school_id;

	}

	public Long getUser_id() {

		return user_id;

	}

	public String getSurname() {

		return surname;

	}

	public String getFirstname() {
		return firstname;
	}

	public String getBefore_surname() {

		return before_surname;

	}

	public String getAfter_surname() {

		return after_surname;

	}

	public String getAbbreviation() {
		return abbreviation;
	}


	public String getExternal_id() {
		return external_id;
	}


	public String getGrade() {
		return grade;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setSchool_id(Long school_id) {
		this.school_id = school_id;
	}


	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public void setBefore_surname(String before_surname) {
		this.before_surname = before_surname;
	}


	public void setAfter_surname(String after_surname) {
		this.after_surname = after_surname;
	}


	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}


	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setSynchronized(boolean is_synchronized){
		this.is_synchronized = is_synchronized;
	}


    public boolean isSynchronized() {
        return is_synchronized;
    }

	public String getFullName() {
		String name = "";

		if(grade != null && !grade.isEmpty()){
			name += grade + " ";
		}

		if(before_surname != null && !before_surname.isEmpty()){
			name += before_surname + " ";
		}

		name += surname;

		if(after_surname != null && !after_surname.isEmpty()){
			name += " " + after_surname;
		}

		name += ", " + firstname;

		return name;
	}
}