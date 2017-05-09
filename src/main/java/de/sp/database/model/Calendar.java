package de.sp.database.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Martin on 09.05.2017.
 */
public class Calendar {

    private Long id;
    private String title;
    private String description;
    private String short_title;
    private String location;
    private Boolean allDay;
    private Boolean preliminary;

    private Date start;
    private Date end;

    private Integer start_period; // Unterrichtsstunde!
    private Integer end_period;

    private String color;

    private transient ArrayList<CalendarRestriction> restrictions = null;
    private transient ArrayList<Absence> absences = null;



    public Calendar(Long id, String title, String description, String short_title,
                    String location, Boolean allDay,
                    Boolean preliminary, Date start, Date end, Integer start_period,
                    Integer end_period, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.short_title = short_title;
        this.location = location;
        this.allDay = allDay;
        this.preliminary = preliminary;
        this.start = start;
        this.end = end;
        this.start_period = start_period;
        this.end_period = end_period;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_title() {
        return short_title;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public Boolean getPreliminary() {
        return preliminary;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Integer getStart_period() {
        return start_period;
    }

    public Integer getEnd_period() {
        return end_period;
    }

    public String getColor() {
        return color;
    }


    public void setRestrictions(ArrayList<CalendarRestriction> restrictions) {
        this.restrictions = restrictions;
    }

    public ArrayList<CalendarRestriction> getRestrictions() {
        return restrictions;
    }

    public ArrayList<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(ArrayList<Absence> absences) {
        this.absences = absences;
    }
}


