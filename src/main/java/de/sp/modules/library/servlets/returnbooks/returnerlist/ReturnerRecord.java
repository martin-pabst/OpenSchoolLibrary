package de.sp.modules.library.servlets.returnbooks.returnerlist;

import java.util.Date;

@SuppressWarnings("unused")
public class ReturnerRecord {
	private String id;
	private String class_name;

	private transient String firstname;
	private transient String surname;

	private String name;

	private Long student_id;
	private Long teacher_id;
	private boolean isStudent;

	private Integer numberOfBorrowedBooks;
	
	private Date exit_date;

	public ReturnerRecord() {

	}

	public void initStudent() {

		name = "<b>" + surname + "</b>, " + firstname;

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

	public String getId() {
		return id;
	}

	public void initTeacher() {

		name = "<b>" + surname + "</b>, " + firstname;

		isStudent = false;
		class_name = "L";
		id = "t" + teacher_id;
	}

	public Long getStudent_id() {
		return student_id;
	}

	public Long getTeacher_id() {
		return teacher_id;
	}

}
