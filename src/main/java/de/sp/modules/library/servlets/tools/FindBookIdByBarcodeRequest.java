package de.sp.modules.library.servlets.tools;

/**
 * Created by Martin on 07.06.2017.
 */
public class FindBookIdByBarcodeRequest {

    public Long school_id;
    public String barcode;

    public FindBookIdByBarcodeRequest(Long school_id, String barcode) {
        this.school_id = school_id;
        this.barcode = barcode;
    }
}
