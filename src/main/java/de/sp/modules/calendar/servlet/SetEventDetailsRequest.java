package de.sp.modules.calendar.servlet;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Martin on 21.05.2017.
 */
public class SetEventDetailsRequest {

    public Long id;
    public Long school_id;
    public Long school_term_id;
    public String title;
    public String description;
    public String short_title;
    public String location;
    public Boolean allDay;
    public Boolean preliminary;

    public Date start;
    public Date end;

    public Integer start_period; // Unterrichtsstunde!
    public Integer end_period;

    public String color;
    public String backgroundColor;
    public String borderColor;
    public String textColor;

    public Boolean absenceWholeSchool;
    public Boolean absenceNoBigTests;
    public Boolean absenceNoSmallTests;


    public ArrayList<Long> restrictionIndices = new ArrayList<>();
    public ArrayList<Long> absencesSelectedClasses = new ArrayList<>();


}
