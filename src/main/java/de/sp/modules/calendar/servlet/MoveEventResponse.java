package de.sp.modules.calendar.servlet;

/**
 * Created by Martin on 28.05.2017.
 */
public class MoveEventResponse {

    public String status;
    public String message;

    public MoveEventResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
