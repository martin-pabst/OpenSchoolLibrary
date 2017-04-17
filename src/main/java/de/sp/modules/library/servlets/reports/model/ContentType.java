package de.sp.modules.library.servlets.reports.model;

/**
 * Created by Martin on 17.04.2017.
 */
public enum ContentType {
    pdf("Pdf", "application/pdf"), html("Html", "text/html"), docx("Word", "application/msword"), xlsx("Excel", "application/msexcel");

    private String name;
    private String contentType;

    ContentType(String name, String contentType) {
        this.name = name;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }
}
