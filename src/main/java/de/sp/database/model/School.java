package de.sp.database.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class School {

	private long id;

	private String name;

	private String abbreviation;

	private String number;

	private ArrayList<SchoolTerm> schoolTerms = new ArrayList<>();

	public School() {
	}

	public School(long id, String name, String number, String abbreviation) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.abbreviation = abbreviation;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public void addSchoolTerm(SchoolTerm schoolTerm) {

		schoolTerms.add(schoolTerm);

	}

	public ArrayList<SchoolTerm> getSchoolTerms() {
		return schoolTerms;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public SchoolTerm getCurrentSchoolTerm() {

		Date now = Calendar.getInstance().getTime();

		SchoolTerm latestTerm = null;

		for (SchoolTerm st : schoolTerms) {

			Term t = st.getTerm();

			if ((now.after(t.getBegindate()) || now.equals(t.getBegindate()))
					&& (now.before(t.getEnddate()) || now
							.equals(t.getEnddate()))) {
				return st;
			}

			if (latestTerm == null) {
				latestTerm = st;
			} else {
				if (t.getBegindate().after(latestTerm.getTerm().getEnddate())) {
					latestTerm = st;
				}
			}

		}

		return latestTerm;
	}

	public SchoolTerm getSchoolTerm(Long school_term_id) {

		for (SchoolTerm st : schoolTerms) {
			if (st.getId() == school_term_id.longValue()) {
				return st;
			}
		}

		return null;
	}

}
