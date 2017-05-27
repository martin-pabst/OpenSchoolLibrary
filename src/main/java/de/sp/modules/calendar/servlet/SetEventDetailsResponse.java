package de.sp.modules.calendar.servlet;

import de.sp.database.model.Event;

/**
 * Created by Martin on 21.05.2017.
 */
public class SetEventDetailsResponse {

    public Event event;
    public String status;
    public String message;

    public SetEventDetailsResponse(String status, String message, Event event) {
        this.event = event;
        this.status = status;
        this.message = message;
    }
}
