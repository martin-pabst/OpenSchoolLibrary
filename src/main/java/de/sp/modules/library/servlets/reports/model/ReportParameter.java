package de.sp.modules.library.servlets.reports.model;

import com.google.gson.Gson;

/**
 * Created by Martin on 21.04.2017.
 */
public class ReportParameter {

    private ParameterType type;
    private String text;
    private String description;
    private boolean compulsory;

    public ReportParameter(ParameterType type, String text, String description, boolean compulsory) {
        this.type = type;
        this.text = text;
        this.description = description;
        this.compulsory = compulsory;
    }

    public String toJson(){

        StringBuilder sb = new StringBuilder();

        Gson gson = new Gson();

        sb.append("{ \"type\": \"").append(type.getName()).append("\", ");
        sb.append("\"text\": ").append(gson.toJson(text)).append(", ");
        sb.append("\"description\": ").append(gson.toJson(description)).append(", ");
        sb.append("\"compulsory\": \"").append(compulsory ? "true" : "false").append("\"}");

        return sb.toString();
    }
}
