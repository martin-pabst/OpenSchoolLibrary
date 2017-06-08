package de.sp.modules.library.servlets.tools;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 07.06.2017.
 */
public class FindBookIdByBarcodeRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notEmpty = true)
    public String barcode;

    public FindBookIdByBarcodeRequest(Long school_id, String barcode) {
        this.school_id = school_id;
        this.barcode = barcode;
    }
}
