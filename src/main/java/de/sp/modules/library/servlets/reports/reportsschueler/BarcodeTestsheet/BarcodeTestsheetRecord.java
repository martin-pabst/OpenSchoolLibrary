package de.sp.modules.library.servlets.reports.reportsschueler.BarcodeTestsheet;

/**
 * Created by Martin on 25.04.2017.
 */
public class BarcodeTestsheetRecord implements Comparable<BarcodeTestsheetRecord> {

    private String class_name;
    private Long class_id;
    private String studentname;
    private Long student_id;
    private String languagesReligionCurriculum;
    private String book;
    private Long book_id;

    private String barcode;
    private Boolean needed;


    public BarcodeTestsheetRecord(String class_name, Long class_id, String studentname, Long student_id,
                                  String languagesReligionCurriculum, String book, Long book_id, String barcode, Boolean needed) {
        this.class_name = class_name;
        this.class_id = class_id;
        this.studentname = studentname;
        this.student_id = student_id;
        this.languagesReligionCurriculum = languagesReligionCurriculum;
        this.book = book;
        this.book_id = book_id;
        this.needed = needed;

        if(barcode == null){
            barcode = "0";
        }

        while(barcode.length() < 12){
            barcode = "0" + barcode;
        }

        this.barcode = barcode;
    }

    public String getClass_name() {
        return class_name;
    }

    public Long getClass_id() {
        return class_id;
    }

    public String getStudentname() {
        return studentname;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public String getBook() {
        return book;
    }

    public Long getBook_id() {
        return book_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public Boolean getNeeded() {
        return needed;
    }

    @Override
    public int compareTo(BarcodeTestsheetRecord r) {

        if(class_name != null && r.class_name != null){
            if(!class_name.equals(r.class_name))
            return class_name.compareTo(r.class_name);
        }

        if(class_name != null && r.class_name == null){
            return 1;
        }

        if(r.class_name != null && class_name == null){
            return -1;
        }

        if(!(studentname.equals(r.studentname))){
            return studentname.compareTo(r.studentname);
        }

        return needed.compareTo(r.needed);

    }

    public String getLanguagesReligionCurriculum() {
        return languagesReligionCurriculum;
    }
}
