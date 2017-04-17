package de.sp.modules.library.servlets.reports.model;

import com.google.gson.Gson;
import net.sf.jasperreports.engine.JRException;
import org.sql2o.Connection;

import java.io.IOException;
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

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract public byte[] execute(ContentType contentType, List<Long> ids, Long school_id,
                                   Long school_term_id, Connection con) throws IOException, JRException;

    protected String getSQLList(List<Long> ids){

        return ids.stream().map( i -> i.toString()).collect(Collectors.joining(", ", "(", ")"));

    }

    public String toJSon( ){
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();

        sb.append("{\"id\": " + id + ", ");
        sb.append("\"dataType\": " + getDataType().getId() + ", ");
        sb.append("\"name\": " + gson.toJson(getName()) + ", ");
        sb.append("\"description\": " + gson.toJson(getDescription()) + ", ");
        sb.append("\"contentTypes\": " + getContentTypes().stream().map(t -> "\"" + t.toString() + "\"").collect(Collectors.joining(", ", "[", "]")));
        sb.append("}");

        return sb.toString();
    }


}
