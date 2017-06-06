package de.sp.modules.library.reports.reportsother.studentsoutsideasv.borrowoverholidays;

import de.sp.database.statements.StatementStore;
import de.sp.modules.library.reports.model.BaseReport;
import de.sp.modules.library.reports.model.ContentType;
import de.sp.modules.library.reports.model.DataType;
import de.sp.modules.library.reports.model.ReportParameter;
import net.sf.jasperreports.engine.JRException;
import org.sql2o.Connection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Martin on 17.04.2017.
 */
public class ReportStudentsOutsideASV extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.sonstige;
    }

    @Override
    public String getName() {
        return "Schüler/innen, die beim letzten Datenabgleich mit ASV nicht dabei waren";
    }

    @Override
    public String getDescription() {
        return "Gibt die Liste der Schüler/innen aus, die in der letzten importierten ASV-Exportdatei nicht enthalten waren.";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.html);
    }

    @Override
    public List<ReportParameter> getParameters() {
        return new ArrayList<>();

    }

    @Override
    public String getFilename() {
        return "Schüler ohne ASV-Bezug";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, List<String> paramerterValues, Connection con,
                        HttpServletResponse response) throws IOException, JRException {

        String sql = StatementStore.getStatement("libraryReports.studentsOutsideASV");

        List<StudentsOutsideASVRecord> studentList = con.createQuery(sql)
                .addParameter("school_id", school_term_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(StudentsOutsideASVRecord.class);

        switch (contentType) {
            case html:
                writeHtml(response, studentList);
                break;
        }


    }

    private void writeHtml(HttpServletResponse response, List<StudentsOutsideASVRecord> studentList) throws IOException {


        beginHtml();

        appendHtmlHeader();

        html.append("<h1>Schüler/innen, die beim letzten ASV-Import nicht in der ASV-Exportdatei enthalten waren</h1>\n");

        Long last_class_id = Long.MAX_VALUE;
        Long last_student_id = null;
        boolean table_open = false;
        boolean firstRow = true;

        for (StudentsOutsideASVRecord student : studentList) {

            if (!secureEquals(student.class_id, last_class_id)) {

                if (last_class_id != null) {
                    html.append("<div style = \"page-break-after:always\"></div>");
                }
                last_class_id = student.class_id;
                if (student.class_name != null) {
                    html.append("<h2>Klasse ").append(student.class_name).append("</h2>\n");
                } else {
                    html.append("<h2>Ohne Klasse</h2>");
                }
            }

            html.append("<div>" + student.firstname + " " + student.surname + "(" + student.book_count + " entliehene Bücher)</div>");

        }

        appendHtmlFooter();

        response.getWriter().println(html.toString());
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

}
