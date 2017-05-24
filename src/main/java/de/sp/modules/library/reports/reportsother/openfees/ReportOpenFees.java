package de.sp.modules.library.reports.reportsother.openfees;

import de.sp.database.statements.StatementStore;
import de.sp.modules.library.reports.model.BaseReport;
import de.sp.modules.library.reports.model.ContentType;
import de.sp.modules.library.reports.model.DataType;
import de.sp.modules.library.reports.model.ReportParameter;
import de.sp.tools.file.FileTool;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.sql2o.Connection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 17.04.2017.
 */
public class ReportOpenFees extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.sonstige;
    }

    @Override
    public String getName() {
        return "Offene Zahlungen";
    }

    @Override
    public String getDescription() {
        return "Liste der offenen Zahlungen, gruppiert je Klasse und Schüler/in";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.pdf, ContentType.html);
    }

    @Override
    public List<ReportParameter> getParameters() {
        return new ArrayList<>();

    }

    @Override
    public String getFilename() {
        return "Offene_Zahlungen";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, List<String> paramerterValues, Connection con,
                        HttpServletResponse response) throws IOException, JRException {

        String sql = StatementStore.getStatement("libraryReports.openFees");

        List<OpenFeeRecord> openFees = con.createQuery(sql)
                .addParameter("school_id", school_term_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(OpenFeeRecord.class);

        consolidateOpenFees(openFees);

        switch (contentType) {
            case pdf:
                writePdf(response, openFees);
                break;
            case html:
                writeHtml(response, openFees);
                break;
        }


    }

    private void consolidateOpenFees(List<OpenFeeRecord> openFees) {

        Collections.sort(openFees);

        Long student_id = null;
        double sum = 0;

        ArrayList<OpenFeeRecord> openFeesForCurrentStudent = new ArrayList<>();

        for (OpenFeeRecord openFee : openFees) {

            if (student_id == null) {

                student_id = openFee.student_id;

            } else if (!student_id.equals(openFee.student_id)) {

                for (OpenFeeRecord ofr : openFeesForCurrentStudent) {
                    ofr.sum = sum;
                }

                openFeesForCurrentStudent.clear();
                student_id = openFee.student_id;
                sum = 0;
            }

            sum += openFee.amount;
            openFeesForCurrentStudent.add(openFee);
        }

        for (OpenFeeRecord ofr : openFeesForCurrentStudent) {
            ofr.sum = sum;
        }
    }

    private void writeHtml(HttpServletResponse response, List<OpenFeeRecord> openFeeRecords) throws IOException {

        DecimalFormat df = new DecimalFormat("#.00");

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        beginHtml();

        appendHtmlHeader();

        html.append("<h1>Offene Zahlungen</h1>\n");

        Long last_class_id = Long.MAX_VALUE;
        Long last_student_id = null;
        boolean table_open = false;
        boolean firstRow = true;
        Double lastSum = null;

        for (OpenFeeRecord of : openFeeRecords) {

            if (!secureEquals(of.class_id, last_class_id)) {

                if (table_open) {

                    appendSum(df, lastSum);
                    lastSum = null;

                    endHtmlTable();
                }

                if (last_class_id != null) {
                    html.append("<div style = \"page-break-after:always\"></div>");
                }
                last_class_id = of.class_id;
                if (of.class_name != null) {
                    html.append("<h2>Klasse ").append(of.class_name).append("</h2>\n");
                } else {
                    html.append("<h2>Ohne Klasse</h2>");
                }
                last_student_id = null;
                beginHtmlTable("width: 100%; margin-bottom: 3em");
                table_open = true;
                firstRow = true;
            }

            if (!of.student_id.equals(last_student_id)) {

                appendSum(df, lastSum);

                if (of.title != null) {
                    last_student_id = of.student_id;

                    beginHtmlRow();
                    if (firstRow) {
                        beginHtmlCell();
                    } else {
                        beginHtmlCell(5); // for next student
                    }
                    html.append("<span class=\"tableheading\">").append(of.surname).append(", ").append(of.firstname).append("</span>\n");
                    endHtmlCell();
                    if (firstRow) {
                        beginHtmlCell();
                        html.append("<span class=\"tableheading\">Ausleihdatum</span>");
                        endHtmlCell();
                        beginHtmlCell();
                        html.append("<span class=\"tableheading\">Rückgabedatum</span>");
                        endHtmlCell();
                        beginHtmlCell();
                        html.append("<span class=\"tableheading\">Gebühr</span>");
                        endHtmlCell();
                        firstRow = false;
                    }
                    endHtmlRow();
                }
            }

            if (of.title != null) {
                beginHtmlRow();
                
                beginHtmlCell("width: 50%");
                html.append(of.title);
                endHtmlCell();
                
                beginHtmlCell();
                if (of.begindate != null) {
                    html.append(sdf.format(of.begindate));
                }
                endHtmlCell();
                
                beginHtmlCell();
                if (of.return_date != null) {
                    html.append(sdf.format(of.return_date));
                }
                endHtmlCell();
                
                beginHtmlCell();
                if (of.amount != null) {
                    html.append(df.format(of.amount) + " €");
                }
                endHtmlCell();

                endHtmlRow();
            }

            lastSum = of.sum;

        }

        if (table_open) {
            appendSum(df, lastSum);
            endHtmlTable();
        }

        appendHtmlFooter();

        response.getWriter().println(html.toString());
    }

    private void appendSum(DecimalFormat df, Double lastSum) {
        if(lastSum != null) {
            // Row with sum of Money
            beginHtmlRow();
            beginHtmlCell(2);
            endHtmlCell();
            beginHtmlCell("font-weight: bold; text-align: right");
            html.append("Summe:");
            endHtmlCell();
            beginHtmlCell("font-weight: bold");
            if (lastSum != null) {
                html.append(df.format(lastSum) + " €");
            }
            endHtmlCell();
            endHtmlRow();
        }
    }

    private boolean secureEquals(Long id1, Long id2) {
        if (id1 == null) {
            return id2 == null;
        }

        if (id2 == null) {
            return false;
        }

        return id1.equals(id2);
    }

    private void writePdf(HttpServletResponse response, List<OpenFeeRecord> borrowedBooks) throws IOException, JRException {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(borrowedBooks);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/openFees.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        writeBytes(byteStream, response);
    }

}
