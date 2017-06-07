package de.sp.modules.library.servlets.tools;

import java.util.Date;

/**
 * Created by Martin on 07.06.2017.
 */
public class FindBookIdByBarcodeResponse {

    public String status;
    public String message;

    public Long book_id;
    public Date sorted_out_date;

    public FindBookIdByBarcodeResponse(){

    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
