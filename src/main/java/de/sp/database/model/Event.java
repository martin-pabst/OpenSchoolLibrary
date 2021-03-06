package de.sp.database.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 09.05.2017.
 */
public class Event {

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

    private String backgroundColor;
    private String borderColor;
    private String textColor;

    private boolean backgroundRendering = false;

    private boolean editable = false;

    private List<EventRestriction> restrictions = new ArrayList<>();
    private List<Absence> absences = new ArrayList<>();



    public Event(Long school_id, String title, String description, String short_title,
                 String location, Boolean allDay,
                 Boolean preliminary, Date start, Date end, String backgroundColor, String borderColor, String textColor,
                 boolean backgroundRendering) {
        this.school_id = school_id;
        this.title = title;
        this.description = description;
        this.short_title = short_title;
        this.location = location;
        this.allDay = allDay;
        this.preliminary = preliminary;
        this.start = start;
        this.end = end;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.backgroundRendering = backgroundRendering;
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

    public Long getSchool_id() {
        return school_id;
    }

    public List<EventRestriction> getRestrictions() {
        return restrictions;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBackgroundRendering() {
        return backgroundRendering;
    }

    /**
     * Don't remove the parameterless constructor as without it absences and restrictions are
     * not initialized when event is deserialized from database with SQL2o.
     */
    public Event(){

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

    public static Integer getYearMonthIndex(Date date, boolean isStartDate){

        java.util.Calendar cal = java.util.Calendar.getInstance();

        cal.setTime(date);

        int month = cal.get(java.util.Calendar.MONTH);
        int year = cal.get(java.util.Calendar.YEAR);


        /*
          If event.allDay = true and event has duration 1 day then end of event is at 00:00 on the following day.
          To avoid registering such an event with yearMonthIndex of the following month, test:
         */
        if(!isStartDate && cal.get(Calendar.DAY_OF_MONTH) == 1
                && cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0){
            month--;
            if(month < 0){
                month += 12;
                year--;
            }
        }

        return year * 100 + month;

    }

    public static List<Integer> getYearMonthList(Date from, Date to){

        Integer ymiFrom = getYearMonthIndex(from, true);
        Integer ymiTo = to == null ? ymiFrom : getYearMonthIndex(to, false);

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

        if(!absences.contains(absence)){
            absences.add(absence);
        }
    }

    public void addRestriction(EventRestriction restriction){
        if(restrictions == null){
            restrictions = new ArrayList<>();
        }
        restrictions.add(restriction);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean hasAbsences() {

        return absences.size() > 0;

    }

    public boolean hasRestrictions() {

        return restrictions.size() > 0;

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

    public boolean classIsAbsent(Long classId) {

        if(absences == null){
            return false;
        }

        for (Absence absence : absences) {
            if(absence.getClass_id() != null && absence.getClass_id().equals(classId)){
                return true;
            }
        }
        
        return false;
    
    }

    public boolean formIsAbsent(Long form_id) {

        if(absences == null){
            return false;
        }

        for (Absence absence : absences) {
            if(absence.getForm_id() != null && absence.getForm_id().equals(form_id)){
                return true;
            }
        }

        return false;

        
    }

    public void updateAttributes(String title, String description, String short_title,
                 String location, Boolean allDay,
                 Boolean preliminary, Date start, Date end, String backgroundColor, String borderColor, String textColor,
                                 boolean backgroundRendering) {
        this.title = title;
        this.description = description;
        this.short_title = short_title;
        this.location = location;
        this.allDay = allDay;
        this.preliminary = preliminary;
        this.start = start;
        this.end = end;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.backgroundRendering = backgroundRendering;
    }


    public Boolean wholeSchoolIsAbsent() {

        for (Absence absence : absences) {
            if(absence.getSchool_id() != null){
                return true;
            }
        }

        return false;

    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


    public void setAllDay(Boolean allDay) {
        if(allDay != null) {
            this.allDay = allDay;
        }
    }
}


