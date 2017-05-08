package de.sp.database.model;


import java.util.Date;

public class SchoolTerm implements Comparable<SchoolTerm>{
	
	public long id;
	
	public long school_id;

	// name of term, e.g. "2017/18"
	public String name;

	public Date begindate;

	public Date enddate;
	
	transient private School school;
	

	public SchoolTerm(){
		
	}
	
	
	
	public SchoolTerm(long id, School school, String name, Date begindate, Date enddate) {
		super();
		this.id = id;
		this.school_id = school.getId();
		this.name = name;
		this.begindate = begindate;
		this.enddate = enddate;

		this.school = school;
	}



	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}


	public long getId() {
		return id;
	}

    public long getSchool_id() {
        return school_id;
    }

    public String getName() {
        return name;
    }

    public Date getBegindate() {
        return begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

	@Override
	public int compareTo(SchoolTerm st) {

		if(begindate != null && st.begindate != null){
			return begindate.compareTo(st.begindate);
		}

		return 0;

	}
}
