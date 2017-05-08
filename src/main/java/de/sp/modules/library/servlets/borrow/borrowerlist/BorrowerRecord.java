package de.sp.modules.library.servlets.borrow.borrowerlist;

import de.sp.database.model.Languageskill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@SuppressWarnings("unused")
public class BorrowerRecord {
	private String id; // "s" + student_id or "t" + teacher_id
	
	private Long student_id;
	private Long student_school_term_id;
	private Long teacher_id;
	private boolean isStudent;
	
	private String class_name;
	private Long class_id;
	private Integer year_of_school;
	private Long form_id;

	private String firstname;
	private String surname;
	private String before_surname;
	private String after_surname;

	private Date dateofbirth;
	private Long sex_key;
	private Long religion_id;
	private String religion;

	private String name;

	private transient Long language_id;
	private transient String language_name;
	private transient Integer from_year;
	private transient Integer to_year;
	private transient Long languageskill_id;

	private ArrayList<Languageskill> languageskills = new ArrayList<>();
	private String languages = "";

	private String curriculum_name;
	private Long curriculum_id;
	
	private Date exit_date;
	
	// Data next school term
	private Long nst_student_school_term_id;
	private String nst_class_name;
	private Long nst_class_id;
	private Integer nst_year_of_school;
	private Long nst_form_id;
	private Long nst_religion_id;
	private String nst_religion;
	private ArrayList<Languageskill> nst_languageskills = new ArrayList<>();
	private String nst_languages = "";
	private String nst_curriculum_name;
	private Long nst_curriculum_id;
	
	
	public BorrowerRecord() {

	}

	public void initStudent() {

		name = "<b>" + surname + "</b>, " + firstname;

		consolidateWith(this);

		Collections.sort(languageskills);
		
		languageskills.forEach(ls -> languages += ls.getLanguage_name() + " ");
		
		languages = languages.trim();
		
		if(class_name == null){
			if(exit_date == null){
				class_name = "NEU";
			} else {
				class_name = "AUS";				
			}
		}
		
		isStudent = true;
		id = "s" + student_id;

	}

	public void consolidateWith(BorrowerRecord br) {

		if (br.languageskill_id != null) {

			boolean alreadySaved = false;

			for (Languageskill ls : languageskills) {
				if (ls.getId() == br.languageskill_id) {
					alreadySaved = true;
				}
			}

			if (!alreadySaved) {
				Languageskill ls = new Languageskill(br.languageskill_id, student_id,
						br.language_id, br.from_year, br.to_year);

				ls.setLanguage_name(br.language_name);

				languageskills.add(ls);

			}

		}

	}

	public String getId() {
		return id;
	}

	public void initTeacher() {
		
		name = "<b>" + surname + "</b>, " + firstname;
		
		isStudent = false;
		id = "t" + teacher_id;
		
		class_name = "L";
		languages = "---";
		curriculum_name = "---";
		religion = "---";
	}

	public Long getStudent_id() {
		return student_id;
	}

	public Long getTeacher_id() {
		return teacher_id;
	}

	public Long getStudent_school_term_id() {
		return student_school_term_id;
	}

	public boolean isStudent() {
		return isStudent;
	}

	public String getClass_name() {
		return class_name;
	}

	public Long getClass_id() {
		return class_id;
	}

	public Integer getYear_of_school() {
		return year_of_school;
	}

	public Long getForm_id() {
		return form_id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getSurname() {
		return surname;
	}

	public String getBefore_surname() {
		return before_surname;
	}

	public String getAfter_surname() {
		return after_surname;
	}

	public Date getDateofbirth() {
		return dateofbirth;
	}

	public Long getSex_key() {
		return sex_key;
	}

	public Long getReligion_id() {
		return religion_id;
	}

	public String getReligion() {
		return religion;
	}

	public String getName() {
		return name;
	}
	
	public Long getLanguage_id() {
		return language_id;
	}

	public String getLanguage_name() {
		return language_name;
	}

	public Integer getFrom_year() {
		return from_year;
	}

	public Integer getTo_year() {
		return to_year;
	}

	public Long getLanguageskill_id() {
		return languageskill_id;
	}

	public ArrayList<Languageskill> getLanguageskills() {
		return languageskills;
	}

	public String getLanguages() {
		return languages;
	}

	public String getCurriculum_name() {
		return curriculum_name;
	}

	public Long getCurriculum_id() {
		return curriculum_id;
	}

	public Date getExit_date() {
		return exit_date;
	}

	// Data next school term
/*
	private Long nst_student_school_term_id;
	private String nst_class_name;
	private Long nst_class_id;
	private Integer nst_year_of_school;
	private Long nst_form_id;
	private Long nst_religion_id;
	private String nst_religion;
	private ArrayList<Languageskill> nst_languageskills = new ArrayList<>();
	private String nst_languages = "";
	private String nst_curriculum_name;
	private Long nst_curriculum_id;
*/
	public void copyOldAttributesFrom(BorrowerRecord br2) {

		nst_student_school_term_id = br2.nst_student_school_term_id;
		nst_class_name = br2.class_name;
		nst_class_id = br2.class_id;
		nst_year_of_school = br2.year_of_school;
		nst_form_id = br2.form_id;
		nst_religion_id = br2.religion_id;
		nst_religion = br2.religion;
		nst_languageskills = br2.languageskills;
		nst_languages = br2.languages;
		nst_curriculum_name = br2.curriculum_name;
		nst_curriculum_id = br2.curriculum_id;

	}
}
