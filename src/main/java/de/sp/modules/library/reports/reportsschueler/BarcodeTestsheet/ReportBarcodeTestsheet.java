package de.sp.modules.library.reports.reportsschueler.BarcodeTestsheet;

import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.statements.StatementStore;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.borrow.borrowedbooks.BorrowedBookRecord;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.modules.library.reports.model.BaseReport;
import de.sp.modules.library.reports.model.ContentType;
import de.sp.modules.library.reports.model.DataType;
import de.sp.modules.library.reports.model.ReportParameter;
import de.sp.modules.library.reports.reportsschueler.borrowedbooks.BorrowedBooksRecord;
import de.sp.modules.library.reports.reportsschueler.neededbooks.NeededBookRecord;
import de.sp.modules.library.reports.reportsschueler.neededbooks.NeededBooksHelper;
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
public class ReportBarcodeTestsheet extends BaseReport {


    @Override
    public DataType getDataType() {
        return DataType.schueler;
    }

    @Override
    public String getName() {
        return "Barcode-Testblatt";
    }

    @Override
    public String getDescription() {
        return "Druckt je Schüler/in ein Blatt mit den Barcodes aller benötigten und geliehenen Bücher aus.";
    }

    @Override
    public List<ContentType> getContentTypes() {
        return Arrays.asList(ContentType.pdf);
    }

    @Override
    public List<ReportParameter> getParameters() {
        return Arrays.asList();
    }

    @Override
    public String getFilename() {
        return "Barcode-Testseiten";
    }

    @Override
    public void execute(ContentType contentType, List<Long> ids, Long school_id,
                        Long school_term_id, List<String> paramerterValues, Connection con, HttpServletResponse response) throws IOException, JRException {


        List<BorrowedBooksRecord> borrowedBooks = getBorrowedBooks(ids, school_term_id, con);

        List<NeededBookRecord> neededBooks = getNeededBooks(ids, school_id, school_term_id, con);

        ArrayList<BarcodeTestsheetRecord> barcodeRecordList = new ArrayList<>();

        LanguagesCurriculumHelper languagesCurriculumHelper = new LanguagesCurriculumHelper(school_id, school_term_id, con);

        for (BorrowedBooksRecord bb : borrowedBooks) {

            if (bb.book_id != null) {

                String barcode = BookCopyDAO.addLeadingZerosToGetEAN13(bb.barcode);

                barcodeRecordList.add(new BarcodeTestsheetRecord(bb.class_name, bb.class_id,
                        bb.getFirstname() + " " + bb.getSurname(), bb.student_id,
                        languagesCurriculumHelper.getLanguageReligionCurriculum(bb.student_id),
                        bb.getTitle(), bb.book_id,
                        barcode, false));
            }

        }

        AvailableBookFinder availableBookFinder = new AvailableBookFinder(school_id, con);

        for (NeededBookRecord nb : neededBooks) {

            if (nb.getBook_id() != null) {
                String barcode = availableBookFinder.popBarcode(nb.getBook_id());

                barcode = BookCopyDAO.addLeadingZerosToGetEAN13(barcode);

                barcodeRecordList.add(new BarcodeTestsheetRecord(nb.getClass_name(), nb.getClass_id(),
                        nb.getBorrower().getFirstname() + " " + nb.getBorrower().getSurname(),
                        nb.getStudent_id(),
                        nb.getLanguagesReligionCurriculum(),
                        nb.getBook(), nb.getBook_id(), barcode, true));
            }
        }

        Collections.sort(barcodeRecordList);

        switch (contentType) {
            case pdf:
                writePdf(response, barcodeRecordList);
                break;
        }


    }

    private List<NeededBookRecord> getNeededBooks(List<Long> ids, Long school_id, Long school_term_id, Connection con) {
        HashSet<Long> selectedStudentIds = new HashSet<>();

        ids.forEach(selectedStudentIds::add);

        List<BorrowerRecord> schuelerList = LibraryDAO.getBorrowerList(school_id, school_term_id, con, false, false);

        List<NeededBookRecord> neededBooks = new ArrayList<>();

        NeededBooksHelper neededBooksHelper = new NeededBooksHelper(school_id, con, false, false);

        for(BorrowerRecord br: schuelerList){

            if (selectedStudentIds.contains(br.getStudent_id())) {

                List<BorrowedBookRecord> borrowedBooks = LibraryDAO.getBorrowedBooksForStudent(br.getStudent_id(), con);

                HashSet<Long> borrowedBooksIds = new HashSet<>();

                borrowedBooks.forEach(bb -> borrowedBooksIds.add(bb.getBook_id()));

                neededBooks.addAll(neededBooksHelper.getNeededBooks(br, borrowedBooksIds));
            }

        }

        Collections.sort(neededBooks);

        return neededBooks;
    }

    private List<BorrowedBooksRecord> getBorrowedBooks(List<Long> ids, Long school_term_id, Connection con) {
        
        String sql = StatementStore.getStatement("libraryReports.schuelerBorrowedBooks");

        sql = sql.replace(":ids", getSQLList(ids));

        List<BorrowedBooksRecord> books = con.createQuery(sql)
                .addParameter("school_id", school_term_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(BorrowedBooksRecord.class);

      return books;


    }


    private void writePdf(HttpServletResponse response, List<BarcodeTestsheetRecord> records) throws IOException, JRException {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(records);

        JasperReport jasperReport = null;
        JasperDesign jasperDesign = null;
        Map parameters = new HashMap();

        jasperDesign = JRXmlLoader.load(FileTool.getInputStream("reporttemplates/barcodeSheet.jrxml"));
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] byteStream = JasperRunManager.runReportToPdf(jasperReport, parameters, ds);

        writeBytes(byteStream, response);
    }

}
