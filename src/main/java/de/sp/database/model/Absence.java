package de.sp.database.model;

/**
 * Created by Martin on 09.05.2017.
 */
public class Absence {

    private Long id;
    private Long school_id; // only != null if absence is for whole school
    private Long class_id;
    private Long form_id; // Jahrgangsstufe
    private Long event_id;

    private boolean no_written_tests;
    private boolean no_tests;

    transient private Event event;

    public Absence(Long school_id, Long class_id, Long form_id,
                   boolean no_written_tests, boolean no_tests) {
        this.school_id = school_id;
        this.class_id = class_id;
        this.form_id = form_id;
        this.no_written_tests = no_written_tests;
        this.no_tests = no_tests;
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

    public Long getEvent_id() {
        return event_id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getSchool_id() {
        return school_id;
    }

    public boolean getNo_written_tests() {
        return no_written_tests;
    }

    public boolean getNo_tests() {
        return no_tests;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }
}
