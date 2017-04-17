package de.sp.database.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sp.database.valuelists.VLSex;
import de.sp.database.valuelists.ValueNotFoundException;
import de.sp.main.mainframe.MainFrameServlet;

public class Student {

	private Long id;

	private Long school_id;

	private Date dateofbirth;

	private String surname;

	private String firstname;

	private String christian_names;

	private String before_surname;

	private String after_surname;

	private Integer sex_key;

	private String external_id;
	
	private Date exit_date;

	public Student() {
	}

	public Student(Long id, Long school_id, Date dateofbirth, String surname,
			String firstname, String christian_names, String before_surname,
			String after_surname, Integer sex_key, String external_id,
			Date exit_date) {

		this.id = id;
		this.school_id = school_id;
		this.dateofbirth = dateofbirth;
		this.surname = surname;
		this.firstname = firstname;
		this.christian_names = christian_names;
		this.before_surname = before_surname;
		this.after_surname = after_surname;
		this.sex_key = sex_key;
		this.external_id = external_id;
		this.exit_date = exit_date;
	}

	public Long getId() {

		return id;

	}

	public Long getSchool_id() {

		return school_id;

	}

	public Date getDateofbirth() {

		return dateofbirth;

	}

	public String getSurname() {

		return surname;

	}

	public String getFirstname() {

		return firstname;

	}

	public String getChristian_names() {

		return christian_names;

	}

	public String getBefore_surname() {

		return before_surname;

	}

	public String getAfter_surname() {

		return after_surname;

	}

	public String getExternal_id() {

		return external_id;

	}

	public void setSchool_id(Long school_id) {
		this.school_id = school_id;
	}

	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setChristian_names(String christian_names) {
		this.christian_names = christian_names;
	}

	public void setBefore_surname(String before_surname) {
		this.before_surname = before_surname;
	}

	public void setAfter_surname(String after_surname) {
		this.after_surname = after_surname;
	}

	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}

	public Date getExit_date() {
		return exit_date;
	}

	public void setExit_date(Date exit_date) {
		this.exit_date = exit_date;
	}

	public void setSex_key(Integer sex_key) {
		this.sex_key = sex_key;
	}

	public VLSex getSex() {

		try {

			return VLSex.findByKey(sex_key);

		} catch (ValueNotFoundException e) {
			Logger logger = LoggerFactory.getLogger(MainFrameServlet.class);
			logger.error("Student record with id " + id
					+ " is broken: Value for sex is not in valuelist.", e);
		}
		return VLSex.male;
	}

	public Object getSex_key() {
		return sex_key;
	}

	public void setSex(VLSex sex) {
	
		if(sex == null){
			sex_key = null;
		}
		
		sex_key = sex.getKey();
		
	}

}