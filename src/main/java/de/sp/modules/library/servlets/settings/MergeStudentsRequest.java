package de.sp.modules.library.servlets.settings;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 30.04.2017.
 */
public class MergeStudentsRequest extends BaseRequestData{

/*
    school_id: global_school_id,
    student1_id: selectedStudents[0].student_id,
    student2_id: selectedStudents[1].student_id,
    deleteStudent12: deleteStudent1 ? 1 : 2
*/
    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long student1_id;

    @Validation(notNull = true)
    public Long student2_id;

    public Integer deleteStudent12;

}
