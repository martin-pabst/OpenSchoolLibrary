package de.sp.modules.library.servlets.reports.model;

import com.google.gson.Gson;
import net.sf.jasperreports.engine.JRException;
import org.sql2o.Connection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Martin on 17.04.2017.
 */
abstract public class BaseReport {

    abstract public DataType getDataType();
    abstract public String getName();
    abstract public String getDescription();
    abstract public List<ContentType> getContentTypes();
    abstract public List<ReportParameter> getParameters();

    abstract public String getFilename();

    private int id;

    protected StringBuilder html;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract public void execute(ContentType contentType, List<Long> ids, Long school_id,
                                 Long school_term_id, List<String> paramerterValues, Connection con, HttpServletResponse response) throws IOException, JRException;

    protected String getSQLList(List<Long> ids){

        if(ids.size() == 0){
            return "(" + Integer.MAX_VALUE + ") AND (1 = 0)"; // " ... IN ( )" is not allowed in Postgresql
        }

        return ids.stream().map( i -> i.toString()).collect(Collectors.joining(", ", "(", ")"));

    }

    public String toJSon( ){
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();

        sb.append("{\"id\": " + id + ", ");
        sb.append("\"dataType\": " + getDataType().getId() + ", ");
        sb.append("\"name\": " + gson.toJson(getName()) + ", ");
        sb.append("\"description\": " + gson.toJson(getDescription()) + ", ");
        sb.append("\"contentTypes\": " + getContentTypes().stream().map(t -> "\"" + t.toString() + "\"").collect(Collectors.joining(", ", "[", "]")) + ", ");
        sb.append("\"parameters\": " + getParameters().stream().map(t -> t.toJson() ).collect(Collectors.joining(", ", "[", "]")) );
        sb.append("}");

        return sb.toString();
    }


    protected void writeBytes(byte[] byteStream, HttpServletResponse response) throws IOException {
        OutputStream outStream = response.getOutputStream();
        response.setContentLength(byteStream.length);
        outStream.write(byteStream,0,byteStream.length);

    }

    protected void beginHtml(){
        html = new StringBuilder();
    }

    protected void appendHtmlHeader() {

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"de\">");
        html.append("<head>");
        html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        html.append(" <link rel=\"icon\" href=\"/public/modules/login/favicon.ico\">");
        html.append("<title>" + getFilename() + "</title>");
        html.append("<link rel=\"stylesheet\" href=\"/modules/library/css/libraryReports.css\">\n");
        html.append("</head>");
        html.append("<body>");

    }

    protected void beginHtmlTable(){
        html.append("<table>\n");
    }

    protected void endHtmlTable(){
        html.append("</table>\n");
    }

    protected void beginHtmlRow(){
        html.append("<tr>\n");
    }

    protected void endHtmlRow(){
        html.append("</tr>\n");
    }

    protected void beginHtmlCell(){
        html.append("<td>");
    }

    protected void beginHtmlCell(int colspan){
        html.append("<td colspan = \"");
        html.append(colspan);
        html.append("\">");
    }


    protected void endHtmlCell(){
        html.append("</td>\n");
    }

    protected void appendHtmlFooter() {

        html.append("</body>");
        html.append("</html>");

    }




}
