package de.sp.database.model;

public class StudentSchoolTerm {

	private Long id;

	private Long student_id;

	private Long school_term_id;

	private Long class_id;

	private Long curriculum_id;

	public StudentSchoolTerm() {
	}

	public StudentSchoolTerm(Long id, Long student_id, Long school_term_id,
			Long class_id, Long curriculum_id) {

		this.id = id;
		this.student_id = student_id;
		this.school_term_id = school_term_id;
		this.class_id = class_id;
		this.curriculum_id = curriculum_id;
	}

	public Long getId() {

		return id;

	}

	public Long getStudent_id() {

		return student_id;

	}

	public Long getSchool_term_id() {

		return school_term_id;

	}

	public Long getClass_id() {

		return class_id;

	}

	public Long getCurriculum_id() {

		return curriculum_id;

	}

	public void setClass_id(Long class_id) {
		this.class_id = class_id;
	}

	public void setCurriculum_id(Long curriculum_id) {
		this.curriculum_id = curriculum_id;
	}
	
	

}