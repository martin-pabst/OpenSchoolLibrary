package de.sp.database.model;

import java.util.Date;

public class Term {

	private long id;

	private String name;

	private Date begindate;

	private Date enddate;

	public Term() {

	}

	public Term(Long id, String name, Date start, Date end) {
		super();
		this.id = id;
		this.name = name;
		this.begindate = start;
		this.enddate = end;
	}

	public long getId() {
		return id;
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

}
