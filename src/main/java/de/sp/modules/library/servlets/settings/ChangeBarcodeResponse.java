package de.sp.modules.library.servlets.settings;

/**
 * Created by Martin on 29.04.2017.
 */
public class ChangeBarcodeResponse {

    public String status;
    public String message;

    public ChangeBarcodeResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
