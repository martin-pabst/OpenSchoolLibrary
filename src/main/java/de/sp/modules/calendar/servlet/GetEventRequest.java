package de.sp.modules.calendar.servlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 12.05.2017.
 */
public class GetEventRequest extends BaseRequestData {

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Date start;

    @Validation(notNull = true)
    public Date end;

    @Validation(acceptedValues = {"schedule", "tests", "absences"})
    public String type; // one of "schedule", "tests", "absences"

    public GetEventRequest(String school_id, String type, String start, String end) throws ParseException {

        this.school_id = Long.parseLong(school_id);
        this.type = type;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.start = sdf.parse(start);
        this.end = sdf.parse(end);

    }
}
