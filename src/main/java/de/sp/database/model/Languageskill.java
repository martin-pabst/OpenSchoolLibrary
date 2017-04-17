package de.sp.database.model;

public class Languageskill implements Comparable<Languageskill> {

	private Long id;

	private Long student_id;

	private Long subject_id;

	private Integer from_year;

	private Integer to_year;

	private String language_name;

	public Languageskill() {
	}

	public Languageskill(Long id, Long student_id, Long subject_id,
			Integer from_year, Integer to_year) {

		this.id = id;
		this.student_id = student_id;
		this.subject_id = subject_id;
		this.from_year = from_year;
		this.to_year = to_year;
	}

	public Long getId() {

		return id;

	}

	public Long getStudent_id() {

		return student_id;

	}

	public Long getSubject_id() {

		return subject_id;

	}

	public Integer getFrom_year() {

		return from_year;

	}

	public Integer getTo_year() {

		return to_year;

	}

	@Override
	public int compareTo(Languageskill o) {

		if (from_year != null && o.from_year != null) {
			return from_year.compareTo(o.from_year);
		}

		return 0;

	}

	public String getLanguage_name() {
		return language_name;
	}

	public void setLanguage_name(String language_name) {
		this.language_name = language_name;
	}

}