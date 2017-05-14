package de.sp.modules.calendar.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 12.05.2017.
 */
public class GetEventRequest {

    public Long school_id;

    public Date start;
    public Date end;

    public String type; // one of "schedule", "tests", "absences"

    public GetEventRequest(String school_id, String type, String start, String end) throws ParseException {

        this.school_id = Long.parseLong(school_id);
        this.type = type;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.start = sdf.parse(start);
        this.end = sdf.parse(end);

    }
}
