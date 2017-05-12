package de.sp.database.model;

/**
 * Created by Martin on 09.05.2017.
 */
public class CalendarRestriction {

    private Long id;
    private Long school_id;
    private Long calendar_id;
    private Long role_id;
    private Long user_id;

    private transient Calendar calendar;

    public CalendarRestriction(Long id, Long school_id,
                               Long calendar_id, Long role_id, Long user_id) {
        this.id = id;
        this.school_id = school_id;
        this.calendar_id = calendar_id;
        this.role_id = role_id;
        this.user_id = user_id;
    }

    public CalendarRestriction(Long school_id, Long role_id, Long user_id, Calendar calendar) {
        this.school_id = school_id;
        this.role_id = role_id;
        this.user_id = user_id;
        this.calendar = calendar;
        this.calendar_id = calendar.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getCalendar_id() {
        return calendar_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Long getSchool_id() {
        return school_id;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCalendar_id(Long calendar_id) {
        this.calendar_id = calendar_id;
    }
}
