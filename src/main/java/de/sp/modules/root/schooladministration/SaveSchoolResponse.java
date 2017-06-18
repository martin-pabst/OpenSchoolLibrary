package de.sp.modules.root.schooladministration;

import de.sp.database.model.School;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveSchoolResponse {

    public String status; // "error" or "success"
    public String message; // if status == "error"

    public School school;

    public SaveSchoolResponse(String status, String message, School school) {
        this.status = status;
        this.message = message;
        this.school = school;
    }
}
