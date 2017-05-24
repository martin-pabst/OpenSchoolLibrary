package de.sp.modules.library.reports.model;

/**
 * Created by Martin on 17.04.2017.
 */
public enum ContentType {
    pdf("Pdf", "application/pdf", "pdf"), html("Html", "text/html", "html"), docx("Word", "application/msword", "docx"), xlsx("Excel", "application/msexcel", "xlsx");

    private String name;
    private String contentType;
    private String fileEnding;

    ContentType(String name, String contentType, String fileEnding) {
        this.name = name;
        this.contentType = contentType;
        this.fileEnding = fileEnding;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileEnding() {
        return fileEnding;
    }
}
