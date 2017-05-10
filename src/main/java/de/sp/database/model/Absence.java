package de.sp.database.model;

/**
 * Created by Martin on 09.05.2017.
 */
public class Absence {

    private Long id;
    private Long school_id; // only != null if absence is for whole school
    private Long class_id;
    private Long form_id; // Jahrgangsstufe
    private Long calendar_id;

    private boolean no_written_tests;
    private boolean no_tests;

    transient private Calendar calendar;

    public Absence(Long id, Long school_id, Long class_id, Long form_id,
                   boolean no_written_tests, boolean no_tests, Long calendar_id) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.form_id = form_id;
        this.no_written_tests = no_written_tests;
        this.no_tests = no_tests;
        this.calendar_id = calendar_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClass_id() {
        return class_id;
    }

    public void setClass_id(Long class_id) {
        this.class_id = class_id;
    }

    public Long getForm_id() {
        return form_id;
    }

    public void setForm_id(Long form_id) {
        this.form_id = form_id;
    }

    public Long getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(Long calendar_id) {
        this.calendar_id = calendar_id;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Long getSchool_id() {
        return school_id;
    }

    public boolean isNo_written_tests() {
        return no_written_tests;
    }

    public boolean isNo_tests() {
        return no_tests;
    }
}
