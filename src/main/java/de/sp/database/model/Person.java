package de.sp.database.model;


public class Person {

	private Long id;

	private String surname;

	private String firstname;

	private String before_surname;

	private String after_surname;

	private String grade;

	private Long student_id;

	private Long person_type_id;

	private Long address_id;

	private Boolean is_legal_parent;

	private Boolean is_primary_legal_parent;

	public Person() {
	}

	public Person(Long id, String surname, String firstname,
			String before_surname, String after_surname, String grade,
			Long student_id, Long person_type_id, Long address_id,
			Boolean is_legal_parent, Boolean is_primary_legal_parent) {

		this.id = id;
		this.surname = surname;
		this.firstname = firstname;
		this.before_surname = before_surname;
		this.after_surname = after_surname;
		this.grade = grade;
		this.student_id = student_id;
		this.person_type_id = person_type_id;
		this.address_id = address_id;
		this.is_legal_parent = is_legal_parent;
		this.is_primary_legal_parent = is_primary_legal_parent;
	}

	public Long getId() {

		return id;

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

	public String getGrade() {

		return grade;

	}

	public Long getStudent_id() {

		return student_id;

	}

	public Long getPerson_type_id() {

		return person_type_id;

	}

	public Long getAddress_id() {

		return address_id;

	}

	public Boolean getIs_legal_parent() {

		return is_legal_parent;

	}

	public Boolean getIs_primary_legal_parent() {
		return is_primary_legal_parent;
	}

}