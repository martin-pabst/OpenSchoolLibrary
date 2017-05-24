package de.sp.modules.library.reports.reportsother.openfees;

import de.sp.tools.objects.CompareTool;

import java.util.Date;

/**
 * Created by Martin on 17.04.2017.
 */
public class OpenFeeRecord implements Comparable<OpenFeeRecord> {

    public String firstname;
    public String surname;
    public Long student_id;
    public String class_name;
    public Long class_id;
    public String title;
    public Date begindate;
    public Date enddate;
    public Date return_date;
    public Double amount;
    public String remarks;

    public Double sum; // sum of fees for one student

    public OpenFeeRecord() {

    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public Long getClass_id() {
        return class_id;
    }

    public String getTitle() {
        return title;
    }

    public Date getBegindate() {
        return begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public Double getSum() {
        return sum;
    }

    @Override
    public int compareTo(OpenFeeRecord o) {
        return CompareTool.compare(
                class_name, o.class_name,
                surname, o.surname,
                firstname, o.firstname,
                student_id, o.student_id,
                title, o.title
        );
    }
}
