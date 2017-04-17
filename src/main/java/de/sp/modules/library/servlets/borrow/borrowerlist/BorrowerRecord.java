package de.sp.modules.library.servlets.borrow.borrowerlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import de.sp.database.model.Languageskill;

@SuppressWarnings("unused")
public class BorrowerRecord {
	private String id; // "s" + student_id or "t" + teacher_id
	
	private Long student_id;
	private Long teacher_id;
	private boolean isStudent;
	
	private String class_name;
	private Integer year_of_school;
	private Long form_id;

	private transient String firstname;
	private transient String surname;

	private String name;

	private transient String term_name;
	private transient Long term_id;

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
	

	public BorrowerRecord() {

	}

	public void initStudent() {

		name = "<b>" + surname + "</b>, " + firstname;

		consolidateWith(this);

		Collections.sort(languageskills);
		
		languageskills.forEach(ls -> languages += ls.getLanguage_name() + " ");
		
		languages.trim();
		
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
	}

	public Long getStudent_id() {
		return student_id;
	}

	public Long getTeacher_id() {
		return teacher_id;
	}



}
