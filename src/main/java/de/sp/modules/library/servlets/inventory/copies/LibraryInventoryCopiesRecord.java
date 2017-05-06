package de.sp.modules.library.servlets.inventory.copies;

import java.util.Date;

public class LibraryInventoryCopiesRecord {

	// book_copy.id, book_copy.book_id, book_copy.barcode,
	// book_copy.edition, borrows.begindate,
	// student.firstname as student_firstname, student.surname as
	// student_surname,
	// teacher.christian_names as teacher_firstname, teacher.surname as
	// teacher_surname,
	// class.name as class_name

	private Long id;

	private Long book_id;

	private String barcode;

	private String edition;

	private String purchase_date;

	private Date beginDate;

	private String student_firstname;

	private String student_surname;

	private String teacher_firstname;

	private String teacher_surname;
	
	private Long teacher_id;

	private String class_name;
	
	private Integer year_of_school;

	private String borrower;

	private String borrowerType; // "student" or "teacher" or "inventory"

	public LibraryInventoryCopiesRecord() {

	}

	public void init() {

		borrower = "";

		if (student_surname != null && !student_surname.isEmpty()) {

			borrowerType = "student";

			if (student_firstname != null) {
				borrower = student_firstname;
			}

			borrower += " <b>" + student_surname + "</b>";

		} else if (teacher_surname != null && !teacher_surname.isEmpty()) {

			if (teacher_firstname != null) {
				borrower = teacher_firstname;
			}

			borrower += " <b>" + teacher_surname + "</b>";

		} else {
			borrowerType = "inventory";
			borrower = "Im Lager";
		}
		
		if(teacher_id != null){
			class_name = "L";
		}
		
	}


	public Long getId() {
		return id;
	}

	public Long getBook_id() {
		return book_id;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getEdition() {
		return edition;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public String getStudent_firstname() {
		return student_firstname;
	}

	public String getStudent_surname() {
		return student_surname;
	}

	public String getTeacher_firstname() {
		return teacher_firstname;
	}

	public String getTeacher_surname() {
		return teacher_surname;
	}

	public String getClass_name() {
		return class_name;
	}

	public String getBorrower() {
		return borrower;
	}

	public String getBorrowerType() {
		return borrowerType;
	}

	public Integer getYear_of_school() {
		return year_of_school;
	}

	public String getPurchase_date() { return purchase_date; }

	public boolean hasGreaterYearOfSchool(LibraryInventoryCopiesRecord other){
		if(year_of_school != null && other.year_of_school != null){
			return year_of_school > other.year_of_school;
		} else {
			return false;
		}
	}
	
	public void copyClassFrom(LibraryInventoryCopiesRecord other){
		class_name = other.class_name;
		year_of_school = other.year_of_school;
	}
}
