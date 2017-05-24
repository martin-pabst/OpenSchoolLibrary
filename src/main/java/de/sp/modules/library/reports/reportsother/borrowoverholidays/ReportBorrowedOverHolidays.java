package de.sp.modules.library.reports.reportsother.borrowoverholidays;

import de.sp.database.statements.StatementStore;
import de.sp.modules.library.reports.model.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 17.04.2017.
 */
public class ReportBorrowedOverHolidays extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.sonstige;
    }

    @Override
    public String getName() {
        return "Bücher, die über die Ferien ausgeliehen wurden";
    }

    @Override
    public String getDescription() {
        return "Gibt die Liste der über die Ferien entliehenen Bücher aus, gruppiert je Klasse und Schüler/in";
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
        return "Ferienausleihen";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, List<String> paramerterValues, Connection con,
                        HttpServletResponse response) throws IOException, JRException {

        String sql = StatementStore.getStatement("libraryReports.borrowedBooksOverHolidays");

        List<BorrowedOverHolidaysRecord> borrowedBooks = con.createQuery(sql)
                .addParameter("school_id", school_term_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(BorrowedOverHolidaysRecord.class);

        switch (contentType) {
            case pdf:
                writePdf(response, borrowedBooks);
                break;
            case html:
                writeHtml(response, borrowedBooks);
                break;
        }


    }

    private void writeHtml(HttpServletResponse response, List<BorrowedOverHolidaysRecord> borrowedBooks) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        beginHtml();

        appendHtmlHeader();

        html.append("<h1>Ferienausleihen</h1>\n");

        Long last_class_id = Long.MAX_VALUE;
        Long last_student_id = null;
        boolean table_open = false;
        boolean firstRow = true;

        for (BorrowedOverHolidaysRecord br : borrowedBooks) {

            if (!secureEquals(br.class_id, last_class_id)) {

                if(table_open){
                    endHtmlTable();
                }

                if (last_class_id != null) {
                    html.append("<div style = \"page-break-after:always\"></div>");
                }
                last_class_id = br.class_id;
                if (br.class_name != null) {
                    html.append("<h2>Klasse ").append(br.class_name).append("</h2>\n");
                } else {
                    html.append("<h2>Ohne Klasse</h2>");
                }
                last_student_id = null;
                beginHtmlTable();
                table_open = true;
                firstRow = true;
            }

            if (!br.student_id.equals(last_student_id)) {
                if (br.title != null) {
                    last_student_id = br.student_id;

                    beginHtmlRow();
                    if(firstRow){
                        beginHtmlCell();
                    } else {
                        beginHtmlCell(2);
                    }
                    html.append("<span class=\"tableheading\">").append(br.surname).append(", ").append(br.firstname).append("</span>\n");
                    endHtmlCell();
                    if(firstRow){
                        beginHtmlCell();
                        html.append("<span class=\"tableheading\">Ausleihdatum</span>");
                        endHtmlCell();
                        firstRow = false;
                    }
                    endHtmlRow();
                }
            }

            if (br.title != null) {
                beginHtmlRow();
                beginHtmlCell();
                html.append(br.title);
                endHtmlCell();
                beginHtmlCell();
                if(br.begindate != null) {
                    html.append(sdf.format(br.begindate));
                }
                endHtmlCell();
                endHtmlRow();
            }

        }

        if(table_open){
            endHtmlTable();
        }

        appendHtmlFooter();

        response.getWriter().println(html.toString());
    }

    private boolean secureEquals(Long id1, Long id2){
        if(id1 == null){
            return id2 == null;
        }

        if(id2 == null){
            return false;
        }

        return id1.equals(id2);
    }

    private void writePdf(HttpServletResponse response, List<BorrowedOverHolidaysRecord> borrowedBooks) throws IOException, JRException {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(borrowedBooks);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/borrowedOverHolidays.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        writeBytes(byteStream, response);
    }

}
