package de.sp.modules.library.servlets.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BookCopyDAO;
import de.sp.database.model.User;
import de.sp.main.services.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.servlets.inventory.copies.BookCopyInfoRecord;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.server.ErrorResponse;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LibraryToolsServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm").create();

        String responseString = "";

        String command = getLastURLPart(request);

        try (Connection con = ConnectionPool.beginTransaction()) {

            try {

                switch (command) {

                    case "findBookIdByBarcode":

                        FindBookIdByBarcodeRequest fbr = gson.fromJson(postData, FindBookIdByBarcodeRequest.class);

                        fbr.validate(ts);

                        user.checkPermission(LibraryModule.PERMISSION_BORROW,
                                fbr.school_id);

                        FindBookIdByBarcodeResponse fbrp = findBookIdByBarcode(fbr, con);



                        responseString = gson.toJson(fbrp);

                        break;


                }

                con.commit(true);

            } catch (Exception ex) {
                logger.error("Error serving Request", ex);
                con.rollback();
                responseString = gson.toJson(new ErrorResponse("Fehler: " + ex.toString()));
            }

        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(responseString);

    }

    private FindBookIdByBarcodeResponse findBookIdByBarcode(FindBookIdByBarcodeRequest fbr, Connection con) {

        FindBookIdByBarcodeResponse response = BookCopyDAO.findBookIdByBarcode(fbr.barcode, fbr.school_id, con);

        if(response == null || response.book_id == null){
            response = new FindBookIdByBarcodeResponse();
            response.setStatus("error");
            response.setMessage("Das Buch mit dem Barcode " + fbr.barcode + " ist nicht in der Datenbank enthalten.");
            return response;
        }

        if(response.sorted_out_date != null){

            List<BookCopyInfoRecord> bookInfo = BookCopyDAO.getBookCopyInInfo(fbr.school_id, fbr.barcode, con);
            String bookInformation = "";

            if(bookInfo != null && bookInfo.size() == 1){
                BookCopyInfoRecord bcir = bookInfo.get(0);
                bookInformation = "<div><b>Informationen zum Buch:</b></div>";
                bookInformation += "<div><b>Titel:</b> " + bcir.getTitle() + "</div>";
                bookInformation += "<div><b>Autor:</b> " + bcir.getAuthor() + "</div>";
            }



            response.setStatus("error");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            response.setMessage("Das Buch mit dem Barcode " + fbr.barcode + " wurde am "
                    + sdf.format(response.sorted_out_date) + " ausgemustert." + bookInformation);
            return response;
        }

        response.setStatus("success");
        return response;

    }


}
