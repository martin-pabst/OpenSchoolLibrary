package de.sp.modules.calendar.servlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Martin on 21.05.2017.
 */
public class SetEventDetailsRequest extends BaseRequestData {

    // null if new event
    public Long id;

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long school_term_id;

    @Validation(notEmpty = true, maxLength = 100)
    public String title;

    @Validation(maxLength = 1000)
    public String description;

    @Validation(maxLength = 20)
    public String short_title;

    @Validation(maxLength = 120)
    public String location;

    public Boolean allDay;
    public Boolean preliminary;

    @Validation(notNull = true)
    public Date start;
    public Date end;

    public Integer start_period; // Unterrichtsstunde!
    public Integer end_period;

    public String color;
    public String backgroundColor;
    public String borderColor;
    public String textColor;

    public boolean backgroundRendering;

    public Boolean absenceWholeSchool;
    public Boolean absenceNoBigTests;


    public Boolean absenceNoSmallTests;
    public ArrayList<Long> restrictionIndices = new ArrayList<>();


    public ArrayList<Long> absencesSelectedClasses = new ArrayList<>();
}
