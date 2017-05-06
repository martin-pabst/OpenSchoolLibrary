package de.sp.modules.library.servlets.settings;

/**
 * Created by Martin on 06.05.2017.
 */
public class SortOutResponse {

    public String status;
    public String message;

    public SortOutResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
