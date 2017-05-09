package de.sp.modules.library.servlets.reports.reportsschueler.neededbooks;

import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BorrowedBookRecord;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.modules.library.servlets.reports.model.*;
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
import java.util.*;

/**
 * Created by Martin on 17.04.2017.
 */
public class ReportNeededBooks extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.schueler;
    }

    @Override
    public String getName() {
        return "Liste der benötigten Bücher";
    }

    @Override
    public String getDescription() {
        return "Gibt die Liste der benötigten Bücher aus, gruppiert je Klasse und Schüler/in";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.pdf, ContentType.html);
    }

    @Override
    public List<ReportParameter> getParameters() {
        return Arrays.asList(new ReportParameter(ParameterType.typeBoolean, "Schüler ohne benötigte Bücher weglassen",
                "Hier das Häkchen setzen, wenn Schüler, die schon alle benötigten Bücher entliehen haben, " +
                        "gar nicht im Report erscheinen sollen", true),
                new ReportParameter(ParameterType.typeBoolean, "Benötigte Bücher fürs <span style=\"font-weight: bold\">kommende</span> Schuljahr",
                        "Es werden die Bücher aufgelistet, die die Schülerin/der Schüler in dem Schuljahr benötigen wird, das dem rechts oben ausgewählten Schuljahr folgt.", true)
                );
    }

    @Override
    public String getFilename() {
        return "Benötigte Bücher";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, List<String> parameterValues, Connection con, HttpServletResponse response) throws IOException, JRException {

        boolean schuelerOhneBenoetigteBuecherWeglassen = true;
        boolean neededBooksForNextTerm = false;

        if(parameterValues != null && parameterValues.size() == getParameters().size()){
            String parameter1 = parameterValues.get(0);
            if(parameter1 != null && parameter1.equals("false")){
                schuelerOhneBenoetigteBuecherWeglassen = false;
            }
            String parameter2 = parameterValues.get(1);
            if(parameter2 != null && parameter2.equals("true")){
                neededBooksForNextTerm = true;
            }
        }

        HashSet<Long> selectedStudentIds = new HashSet<>();

        ids.forEach(selectedStudentIds::add);

        /**
         * Option to print needed books for next schoolyear
         */
        List<BorrowerRecord> schuelerList = LibraryDAO.getBorrowerList(school_id, school_term_id, con, false, true);

        List<NeededBookRecord> neededBooks = new ArrayList<>();

        NeededBooksHelper neededBooksHelper =
                new NeededBooksHelper(school_id, con, schuelerOhneBenoetigteBuecherWeglassen, neededBooksForNextTerm);

        for(BorrowerRecord br: schuelerList){

            if (selectedStudentIds.contains(br.getStudent_id())) {

                List<BorrowedBookRecord> borrowedBooks = LibraryDAO.getBorrowedBooksForStudent(br.getStudent_id(), con);

                HashSet<Long> borrowedBooksIds = new HashSet<>();

                borrowedBooks.forEach(bb -> borrowedBooksIds.add(bb.getBook_id()));

                neededBooks.addAll(neededBooksHelper.getNeededBooks(br, borrowedBooksIds));
            }

        }

        Collections.sort(neededBooks);


        switch (contentType) {
            case pdf:
                writePdf(response, neededBooks, schuelerOhneBenoetigteBuecherWeglassen);
                break;
            case html:
                writeHtml(response, neededBooks, schuelerOhneBenoetigteBuecherWeglassen);
                break;
        }


    }

    private void writeHtml(HttpServletResponse response, List<NeededBookRecord> neededBooks, boolean omitPupilsWithoutMissingBooks) throws IOException {

        beginHtml();

        appendHtmlHeader();

        Long last_class_id = null;
        Long last_student_id = null;
        boolean table_open = false;
        boolean firstRow = true;

        for (NeededBookRecord br : neededBooks) {

            if (!secureEquals(br.getClass_id(), last_class_id)) {

                if(table_open){
                    endHtmlTable();
                }

                if (last_class_id != null) {
                    html.append("<div style = \"page-break-after:always\"></div>");
                }
                last_class_id = br.getClass_id();
                if (br.getClass_name() != null) {
                    html.append("<h2>Benötigte Bücher Klasse ").append(br.getClass_name()).append("</h2>\n");
                } else {
                    html.append("<h2>Benötigte Bücher von Schüler/innen ohne Klassenzuordnung</h2>");
                }
                last_student_id = null;
                beginHtmlTable();
                table_open = true;
                firstRow = true;
            }

            if (!br.getStudent_id().equals(last_student_id)) {
                if (!omitPupilsWithoutMissingBooks || br.getBook() != null) {
                    last_student_id = br.getStudent_id();

                    beginHtmlRow();
                    if(firstRow){
                        beginHtmlCell();
                    } else {
                        beginHtmlCell(2);
                    }
                    html.append("<span class=\"tableheading\">").append(br.getStudentname())
                            .append(" ")
                            .append(br.getLanguagesReligionCurriculum())
                            .append("</span>\n");
                    endHtmlCell();
                    if(firstRow){
                        beginHtmlCell();
                        html.append("<span class=\"tableheading\">Fach</span>");
                        endHtmlCell();
                        firstRow = false;
                    }
                    endHtmlRow();
                }
            }

            if (br.getBook() != null) {
                beginHtmlRow();
                beginHtmlCell();
                html.append(br.getBook());
                endHtmlCell();
                beginHtmlCell();
                if(br.getSubject() != null) {
                    html.append(br.getSubject());
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

    private void writePdf(HttpServletResponse response, List<NeededBookRecord> neededBooks, boolean schuelerOhneFehlendeBuecherDrucken) throws IOException, JRException {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(neededBooks);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/neededBooks.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        writeBytes(byteStream, response);
    }


}
