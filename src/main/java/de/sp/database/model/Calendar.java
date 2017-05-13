package de.sp.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 09.05.2017.
 */
public class Calendar {

    private Long id;
    private Long school_id;
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
    private String backgroundColor;
    private String borderColor;
    private String textColor;

    private boolean editable = false;

    private ArrayList<CalendarRestriction> restrictions = null;
    private  ArrayList<Absence> absences = null;



    public Calendar(Long school_id, String title, String description, String short_title,
                    String location, Boolean allDay,
                    Boolean preliminary, Date start, Date end, Integer start_period,
                    Integer end_period, String color, String backgroundColor, String borderColor, String textColor) {
        this.school_id = school_id;
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
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;

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

    public Long getSchool_id() {
        return school_id;
    }

    public ArrayList<CalendarRestriction> getRestrictions() {
        return restrictions;
    }

    public ArrayList<Absence> getAbsences() {
        return absences;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * Returns list with values 100*year + month (jan == 0) for each
     * (year/month) in interval [start; end]
     *
     * @return
     */
    public List<Integer> getYearMonthList() {

        return getYearMonthList(start, end);

    }

    public static Integer getYearMonthIndex(Date date){

        java.util.Calendar cal = java.util.Calendar.getInstance();

        cal.setTime(date);
        return cal.get(java.util.Calendar.YEAR) * 100 + cal.get(java.util.Calendar.MONTH);

    }

    public static List<Integer> getYearMonthList(Date from, Date to){

        Integer ymiFrom = getYearMonthIndex(from);
        Integer ymiTo = getYearMonthIndex(to);

        int i = ymiFrom;

        ArrayList<Integer> ymList = new ArrayList<>();

        while(i <= ymiTo){

            ymList.add(i);

            i++;

            if(i % 100 >= 12){
                i = i - (i % 100) + 100;
            }

        }

        return ymList;
    }


    public void addAbsence(Absence absence) {
        if(absences == null){
            absences = new ArrayList<>();
        }

        if(!absences.contains(absence)){
            absences.add(absence);
        }
    }

    public void addRestriction(CalendarRestriction restriction){
        if(restrictions == null){
            restrictions = new ArrayList<>();
        }
        restrictions.add(restriction);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean hasAbsences() {

        return absences != null && absences.size() > 0;

    }

    public boolean hasRestrictions() {

        return restrictions != null && restrictions.size() > 0;

    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    // TODO
    public boolean isTest() {
        return false;
    }
}


