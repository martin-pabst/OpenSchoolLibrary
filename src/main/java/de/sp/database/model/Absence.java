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

    // TODO: rename: no_big_tests
    private boolean no_big_tests;

    // TODO: rename: no_small_tests;
    private boolean no_small_tests;

    transient private Event event;

    public Absence(Long school_id, Long class_id, Long form_id,
                   boolean no_big_tests, boolean no_small_tests) {
        this.school_id = school_id;
        this.class_id = class_id;
        this.form_id = form_id;
        this.no_big_tests = no_big_tests;
        this.no_small_tests = no_small_tests;
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

    public boolean getNo_big_tests() {
        return no_big_tests;
    }

    public boolean getNo_small_tests() {
        return no_small_tests;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }
}
