package de.sp.modules.library.servlets.orderbooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BookDAO;
import de.sp.database.model.Book;
import de.sp.database.model.User;
import de.sp.main.services.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.reports.model.ContentType;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseSave;
import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.word.WordTool;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class LibraryOrderBooksServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();


        String postString = URLDecoder.decode(postData, "UTF-8");

        if (postString.indexOf("{") < 0) {
            response.getWriter().println("<h1>Fehler beim Ausführen des Berichts:<h1>");

        } else {

            try (Connection con = ConnectionPool.beginTransaction()) {


                postString = postString.substring(postString.indexOf('{'));
                OrderBooksRequest orderBooksRequest = gson.fromJson(postString, OrderBooksRequest.class);


                user.checkPermission(LibraryModule.PERMISSION_REPORTS,
                        orderBooksRequest.getSchool_id());


                List<Book> books = BookDAO.getAll(con);

                for (OrderBookRow obr : orderBooksRequest.getSelectedRows()) {

                    for (Book book : books) {
                        if (book.getId() == obr.getId()) {
                            obr.setBook(book);
                            break;
                        }
                    }

                }

                generateDocument(orderBooksRequest, response);

                con.rollback();

            } catch (Exception ex) {
                logger.error(this.getClass().toString() + ": Error serving data",
                        ex);
                String responseString = gson.toJson(new GridResponseSave(
                        GridResponseStatus.error, ex.toString(), null));

                response.setContentType("text/json");
                response.setStatus(HttpServletResponse.SC_OK);

                response.getWriter().println(responseString);
            }

        }

    }

    private void generateDocument(OrderBooksRequest orderBooksRequest, HttpServletResponse response) {

        try {
            String tempFilename = "temp/Bestellung.docx";

            WordTool wt = new WordTool("configuration/BestellungVorlage.docx", tempFilename);

            wt.replace("$AN", orderBooksRequest.getAddress());

            wt.write();

            response.setStatus(HttpServletResponse.SC_OK);

            response.setContentType(ContentType.docx.getContentType());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            String filename = "Buchbestellung_" + sdf.format(Calendar.getInstance().getTime()) + ".docx";

            response.setHeader("Content-Disposition", "filename=\"" + filename + "\"");

            writeFile(tempFilename, response);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeFile(String tempFilename, HttpServletResponse response) {

        int BUFF_SIZE = 1024;
        byte[] buffer = new byte[BUFF_SIZE];
        File file = new File(tempFilename);

        try {
            response.setContentLength((int) file.length());
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);

            try {
                int byteRead = 0;
                while ((byteRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, byteRead);

                }
                os.flush();
            } catch (Exception excp) {
                excp.printStackTrace();
            } finally {
                os.close();
                fis.close();
            }

        } catch(IOException ex){
            ex.printStackTrace();
        }

    }


}
