package de.sp.modules.calendar.servlet;

/**
 * Created by Martin on 28.05.2017.
 */
public class RemoveEventResponse {

    public String status;
    public String message;

    public RemoveEventResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
