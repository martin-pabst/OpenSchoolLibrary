package de.sp.modules.library.reports.reportsschueler.BarcodeTestsheet;

import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.HashMap;
import java.util.List;

/**
 * Created by martin on 26.04.2017.
 */
public class LanguagesCurriculumHelper {


    private HashMap<Long, LanguageCurriculumRecord> recordMap = new HashMap<>();


    public LanguagesCurriculumHelper(Long school_id, Long school_term_id, Connection con){

        String sql = StatementStore.getStatement("libraryReports.studentsLanguagesCurriculum");

        List<LanguageCurriculumRecord> languageCurriculumRecords = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(LanguageCurriculumRecord.class);

        for (LanguageCurriculumRecord lcr : languageCurriculumRecords) {

            LanguageCurriculumRecord lcrConsolidated = recordMap.get(lcr.student_id);

            if(lcrConsolidated == null){

                recordMap.put(lcr.student_id, lcr);

            } else {

                lcrConsolidated.language = lcrConsolidated.language + " " + lcr.language;

            }

        }


    }

    public String getLanguageReligionCurriculum(Long student_id){

        LanguageCurriculumRecord lcr = recordMap.get(student_id);

        if(lcr == null){
            return "";
        }

        String s = "(" + lcr.language;

        if(lcr.religion != null){
            s += ", " + lcr.religion;
        }

        if(lcr.curriculum != null){
            s += ", " + lcr.curriculum;
        }

        s += ")";

        return s;
    }


}
