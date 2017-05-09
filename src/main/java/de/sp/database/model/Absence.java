package de.sp.database.model;

/**
 * Created by Martin on 09.05.2017.
 */
public class Absence {

    private Long id;
    private Long class_id;
    private Long form_id;
    private Long calendar_id;

    transient private DBClass dbclass;
    transient private Value form;
    transient private Calendar calendar;

    public Absence(Long id, Long class_id, Long form_id, Long calendar_id) {
        this.id = id;
        this.class_id = class_id;
        this.form_id = form_id;
        this.calendar_id = calendar_id;
    }

    public Absence(Long id, DBClass dbclass, Value form, Calendar calendar) {
        this.id = id;
        this.dbclass = dbclass;
        this.form = form;
        this.calendar = calendar;

        if(dbclass != null){
            class_id = dbclass.getId();
        }

        if(form != null){
            form_id = form.getId();
        }

        if(calendar != null){
            calendar_id = calendar.getId();
        }

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

    public DBClass getDbclass() {
        return dbclass;
    }

    public void setDbclass(DBClass dbclass) {
        this.dbclass = dbclass;
    }

    public Value getForm() {
        return form;
    }

    public void setForm(Value form) {
        this.form = form;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
