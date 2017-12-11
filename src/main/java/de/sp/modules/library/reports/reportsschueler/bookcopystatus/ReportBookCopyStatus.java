package de.sp.modules.library.reports.reportsschueler.bookcopystatus;

import de.sp.database.daos.basic.BookCopyStatusDAO;
import de.sp.database.model.BookCopyStatus;
import de.sp.database.statements.StatementStore;
import de.sp.modules.library.reports.model.BaseReport;
import de.sp.modules.library.reports.model.ContentType;
import de.sp.modules.library.reports.model.DataType;
import de.sp.modules.library.reports.model.ReportParameter;
import de.sp.modules.library.reports.reportsschueler.borrowedbooks.BorrowedBooksRecord;
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
public class ReportBookCopyStatus extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.schueler;
    }

    @Override
    public String getName() {
        return "Entliehene Bücher mit Buchzustand";
    }

    @Override
    public String getDescription() {
        return "Gibt die Liste der entliehenen Bücher und deren Zustand aus, gruppiert je Klasse und Schüler/in";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.pdf);
    }

    @Override
    public List<ReportParameter> getParameters() {
        return new ArrayList<>();
    }

    @Override
    public String getFilename() {
        return "Entliehene Bücher (mit Buchzustand)";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, List<String> paramerterValues, Connection con, HttpServletResponse response) throws IOException, JRException {

        String sql = StatementStore.getStatement("libraryReports.schuelerBorrowedBooks");

        sql = sql.replace(":ids", getSQLList(ids));

        List<BorrowedBooksRecord> borrowedBooks = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(BorrowedBooksRecord.class);

        // remove students without borrowed books
        int i = 0;
        while(i < borrowedBooks.size()) {

            if (borrowedBooks.get(i).getTitle() == null) {
                borrowedBooks.remove(i);
            } else {
                i++;
            }

        }


        HashMap<Long, BorrowedBooksRecord> bookCopyIdToBorrowedBooksMap = new HashMap<>();

        for (BorrowedBooksRecord borrowedBook : borrowedBooks) {
            bookCopyIdToBorrowedBooksMap.put(borrowedBook.book_copy_id, borrowedBook);
        }


        List<BookCopyStatus> statusList = BookCopyStatusDAO.findBySchoolId(school_id);

        for (BookCopyStatus bookCopyStatus : statusList) {
            BorrowedBooksRecord bb = bookCopyIdToBorrowedBooksMap.get(bookCopyStatus.getBook_copy_id());
            if(bb != null){
                bb.getStatusList().add(bookCopyStatus);
            }
        }

        switch (contentType) {
            case pdf:
                writePdf(response, borrowedBooks);
                break;
        }


    }


    private void writePdf(HttpServletResponse response, List<BorrowedBooksRecord> borrowedBooks) throws IOException, JRException {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(borrowedBooks);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/bookstatus/bookStatus.jrxml"));

        JasperDesign subReportDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/bookstatus/bookStatusSubreport.jrxml"));
        JasperReport subReport = JasperCompileManager.compileReport(subReportDesign);

        parameters.put("subreportParameter", subReport);

        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        writeBytes(byteStream, response);
    }

}
