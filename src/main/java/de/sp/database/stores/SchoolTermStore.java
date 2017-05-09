package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SchoolDAO;
import de.sp.database.daos.basic.SchoolTermDAO;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.util.*;

public class SchoolTermStore {

    private List<School> schools = new ArrayList<>();
    private Map<Long, School> schoolKeys = new HashMap<>();

    private Map<Long, ArrayList<SchoolTerm>> schoolIdToSchoolTermListMap = new HashedMap<>();
    private Map<Long, SchoolTerm> schoolTermIds = new HashMap<>();

    private static SchoolTermStore instance;

    private SchoolTermStore() {

    }

    public static SchoolTermStore getInstance() {

        if (instance == null) {

            instance = new SchoolTermStore();

        }

        return instance;
    }

    public List<School> getSchools() {
        return schools;
    }

    public Map<Long, School> getSchoolKeys() {
        return schoolKeys;
    }

    public void loadFromDatabase() {

        try (Connection con = ConnectionPool.open()) {

            schools = SchoolDAO.getAll(con);

            schoolKeys = SchoolDAO.schoolListToKeyMap(schools);

            List<SchoolTerm> allSchoolTermList = SchoolTermDAO.getAll(con);

            for (SchoolTerm schoolTerm : allSchoolTermList) {

                School school = schoolKeys.get(schoolTerm.getSchool_id());
                schoolTerm.setSchool(school);
                school.addSchoolTerm(schoolTerm);

                ArrayList<SchoolTerm> schoolTermList = schoolIdToSchoolTermListMap.get(school.getId());

                if (schoolTermList == null) {
                    schoolTermList = new ArrayList<>();
                    schoolIdToSchoolTermListMap.put(school.getId(), schoolTermList);
                }

                schoolTermList.add(schoolTerm);
                schoolTermIds.put(schoolTerm.getId(), schoolTerm);
            }
        } catch (Exception ex) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(ex.toString(), ex);
        }

    }

    public School getSchoolById(Long school_id) {
        return schoolKeys.get(school_id);
    }

    public SchoolTerm getSchoolTerm(Long schoolTermId) {
        return schoolTermIds.get(schoolTermId);
    }

    public void addSchoolTerm(SchoolTerm schoolTerm) {

        schoolTermIds.put(schoolTerm.getId(), schoolTerm);

        ArrayList<SchoolTerm> schoolTermList = schoolIdToSchoolTermListMap.get(schoolTerm.getSchool_id());

        if (schoolTermList == null) {
            schoolTermList = new ArrayList<>();
            schoolIdToSchoolTermListMap.put(schoolTerm.getSchool_id(), schoolTermList);
        }

        schoolTermList.add(schoolTerm);

    }

    public SchoolTerm getTerm(long school_Id, String termname) {

        ArrayList<SchoolTerm> schoolTermList = schoolIdToSchoolTermListMap.get(school_Id);

        if (schoolTermList == null) {
            return null;
        }

        for (SchoolTerm schoolTerm : schoolTermList) {
            if (schoolTerm.name.equals(termname)) {
                return schoolTerm;
            }
        }

        return null;

    }

    public SchoolTerm getNextSchoolTerm(Long school_id, Long school_term_id) {

        List<SchoolTerm> schoolTermList = new ArrayList<>();
        schoolTermList.addAll(schoolIdToSchoolTermListMap.get(school_id));

        Collections.sort(schoolTermList);

        int i = 0;

        for (SchoolTerm schoolTerm : schoolTermList) {
            if(schoolTerm.getId() == school_term_id){
                break;
            } else {
                i++;
            }
        }

        if(schoolTermList.size() > i + 1){
            return schoolTermList.get(i+1);
        }

        return null;

    }
}
