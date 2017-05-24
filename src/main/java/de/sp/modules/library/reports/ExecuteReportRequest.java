package de.sp.modules.library.reports;

import de.sp.modules.library.reports.model.ContentType;
import de.sp.modules.library.reports.model.DataType;

import java.util.List;

/**
 * Created by Martin on 17.04.2017.
 */
public class ExecuteReportRequest {

//    {"selectedRows":[0,1],"reportId":0,"contentType":"pdf","dataType":1,"school_id":1,"school_term_id":1}

    public List<Long> selectedRows;
    public Long reportId;
    public String contentType;
    public Long dataType;
    public Long school_id;
    public Long school_term_id;
    public List<String> parameterValues;

    public DataType getDataType(){
        return DataType.findById(dataType);
    }

    public ContentType getContentTypeEnum(){
        return ContentType.valueOf(contentType);
    }

    public Long getReportId() {
        return reportId;
    }

    public List<String> getParameterValues() {
        return parameterValues;
    }
}
