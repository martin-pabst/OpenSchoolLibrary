package de.sp.modules.library.reports.reportsother.studentsoutsideasv.borrowoverholidays;

import de.sp.tools.objects.CompareTool;

/**
 * Created by Martin on 17.04.2017.
 */
public class StudentsOutsideASVRecord implements Comparable<StudentsOutsideASVRecord> {

    public Long class_id;
    public Long student_id;
    public String class_name;
    public String firstname;
    public String surname;
    public Integer book_count;

    public StudentsOutsideASVRecord(){

    }

    public Long getClass_id() {
        return class_id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getBook_count() {
        return book_count;
    }

    @Override
    public int compareTo(StudentsOutsideASVRecord o) {
        return CompareTool.compare(class_name, surname ,firstname);
    }
}
