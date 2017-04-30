package de.sp.modules.library.servlets.settings;

/**
 * Created by Martin on 30.04.2017.
 */
public class MergeStudentsRequest {

/*
    school_id: global_school_id,
    student1_id: selectedStudents[0].student_id,
    student2_id: selectedStudents[1].student_id,
    deleteStudent12: deleteStudent1 ? 1 : 2
*/

    public Long school_id;
    public Long student1_id;
    public Long student2_id;
    public Integer deleteStudent12;

}
