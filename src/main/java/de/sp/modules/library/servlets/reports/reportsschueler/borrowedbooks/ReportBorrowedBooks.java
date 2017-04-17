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

import java.io.IOException;
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
        return "Gibt je Schüler/in die Liste der entliehenen Bücher aus";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.pdf);
    }

    @Override
    public byte[] execute(ContentType contentType, List<Long> ids, Long school_id,
                          Long school_term_id, Connection con) throws IOException, JRException {

        String sql = StatementStore.getStatement("library.getClassList");

        sql.replace(":ids", getSQLList(ids));

        List<BorrowedBooksRecord> borrowedBooks = con.createQuery(sql)
                .addParameter("school_id", school_term_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(BorrowedBooksRecord.class);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(borrowedBooks);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        String jrxml = FileTool.readFile("reporttemplates/borrowedBooks.jrxml");

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/borrowedBooks.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        return byteStream;
    }

}
