package de.sp.database.model;



public class SchoolTerm {
	
	public long id;
	
	public long school_id;
	
	public long term_id;
	
	
	transient private School school;
	
	private Term term;

	public SchoolTerm(){
		
	}
	
	
	
	public SchoolTerm(long id, long school_id, long term_id) {
		super();
		this.id = id;
		this.school_id = school_id;
		this.term_id = term_id;
	}



	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}



	public long getId() {
		return id;
	}
	
	
	
}
