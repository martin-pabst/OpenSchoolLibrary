package de.sp.modules.library.reports.model;

import de.sp.modules.library.reports.reportsother.borrowoverholidays.ReportBorrowedOverHolidays;
import de.sp.modules.library.reports.reportsother.openfees.ReportOpenFees;
import de.sp.modules.library.reports.reportsschueler.BarcodeTestsheet.ReportBarcodeTestsheet;
import de.sp.modules.library.reports.reportsschueler.bookcopystatus.ReportBookCopyStatus;
import de.sp.modules.library.reports.reportsschueler.borrowedbooks.ReportBorrowedBooks;
import de.sp.modules.library.reports.reportsschueler.neededbooks.ReportNeededBooks;
import de.sp.modules.library.reports.reportsschueler.paidfees.ReportPaidFees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Martin on 17.04.2017.
 */
public class ReportManager {

    private static ReportManager instance;

    private HashMap<Integer, ArrayList<BaseReport>> reports = new HashMap<>();

    private ReportManager() {

        // Students
        addReport(new ReportBorrowedBooks());
        addReport(new ReportNeededBooks());
        addReport(new ReportBarcodeTestsheet());
        addReport(new ReportPaidFees());
        addReport(new ReportBookCopyStatus());

        //Other
        addReport(new ReportBorrowedOverHolidays());
        addReport(new ReportOpenFees());

    }

    public static ReportManager getInstance() {
        if (instance == null) {
            instance = new ReportManager();
        }
        return instance;
    }

    private void addReport(BaseReport report) {

        Integer dataTypeId = report.getDataType().getId();
        ArrayList<BaseReport> reportList = reports.get(dataTypeId);

        if (reportList == null) {
            reportList = new ArrayList<>();
            reports.put(dataTypeId, reportList);
        }

        int id = reportList.size();
        report.setId(id);

        reportList.add(report);
    }

    public String toJSon() {

        StringBuilder sb = new StringBuilder();

        sb.append("{");

        for (int i = 0; i < DataType.values().length; i++) {

            DataType dataType = DataType.values()[i];

            sb.append("\"" + dataType.getId() + "\": ");

            ArrayList<BaseReport> reportList = reports.get(dataType.getId());

            if(reportList == null){
                sb.append("[]");
            } else {
                sb.append(
                        reportList.stream().map(report -> report.toJSon()).collect(Collectors.joining(", ", "[", "]"))
                );
            }


            if(i < DataType.values().length - 1){
                sb.append(", ");
            }

        }

        sb.append("}");

        return sb.toString();
    }


    public BaseReport getReport(DataType dataType, Long reportId) {

        List<BaseReport> reportList = reports.get(dataType.getId());

        return reportList.get(reportId.intValue());

    }
}
