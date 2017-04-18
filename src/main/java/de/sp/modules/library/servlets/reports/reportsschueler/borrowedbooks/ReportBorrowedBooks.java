package de.sp.modules.library.servlets.reports.reportsschueler.borrowedbooks;

import de.sp.database.statements.StatementStore;
import de.sp.modules.library.servlets.reports.model.BaseReport;
import de.sp.modules.library.servlets.reports.model.ContentType;
import de.sp.modules.library.servlets.reports.model.DataType;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 17.04.2017.
 */
public class ReportBorrowedBooks extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.schueler;
    }

    @Override
    public String getName() {
        return "Liste der entliehenen Bücher";
    }

    @Override
    public String getDescription() {
        return "Gibt die Liste der entliehenen Bücher aus, gruppiert je Klasse und Schüler/in";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.pdf, ContentType.html);
    }

    @Override
    public String getFilename() {
        return "Entliehene Bücher";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, Connection con, HttpServletResponse response) throws IOException, JRException {

        String sql = StatementStore.getStatement("libraryReports.schuelerBorrowedBooks");

        sql = sql.replace(":ids", getSQLList(ids));

        List<BorrowedBooksRecord> borrowedBooks = con.createQuery(sql)
                .addParameter("school_id", school_term_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(BorrowedBooksRecord.class);

        switch (contentType) {
            case pdf:
                writePdf(response, borrowedBooks);
                break;
            case html:
                writeHtml(response, borrowedBooks);
                break;
        }


    }

    private void writeHtml(HttpServletResponse response, List<BorrowedBooksRecord> borrowedBooks) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        beginHtml();

        appendHtmlHeader();

        html.append("<h1>Entliehene Bücher</h1>\n");

        Long last_class_id = null;
        Long last_student_id = null;
        boolean table_open = false;
        boolean firstRow = true;

        for (BorrowedBooksRecord br : borrowedBooks) {

            if (!br.class_id.equals(last_class_id)) {

                if(table_open){
                    endHtmlTable();
                }

                if (last_class_id != null) {
                    html.append("<div style = \"page-break-after:always\"></div>");
                }
                last_class_id = br.class_id;
                html.append("<h2>Klasse ").append(br.class_name).append("</h2>\n");
                last_student_id = null;
                beginHtmlTable();
                table_open = true;
                firstRow = true;
            }

            if (!br.student_id.equals(last_student_id)) {
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

            beginHtmlRow();
            beginHtmlCell();
            html.append(br.title);
            endHtmlCell();beginHtmlCell();
            html.append(sdf.format(br.begindate));
            endHtmlCell();
            endHtmlRow();

        }

        if(table_open){
            endHtmlTable();
        }

        appendHtmlFooter();

        response.getWriter().println(html.toString());
    }

    private void writePdf(HttpServletResponse response, List<BorrowedBooksRecord> borrowedBooks) throws IOException, JRException {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(borrowedBooks);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        String jrxml = FileTool.readFile("reporttemplates/borrowedBooks.jrxml");

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/borrowedBooks.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        writeBytes(byteStream, response);
    }

}
