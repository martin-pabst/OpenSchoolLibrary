package de.sp.modules.library.servlets.borrow.updateBorrowHoliday;

/**
 * Created by Martin on 29.04.2017.
 */
public class UpdateBorrowHolidayResponse {

    public String status;
    public String message;

    public UpdateBorrowHolidayResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
