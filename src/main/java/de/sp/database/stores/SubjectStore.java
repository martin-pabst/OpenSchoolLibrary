package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.model.DatabaseStore;
import de.sp.database.model.Subject;
import org.sql2o.Connection;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by martin on 11.05.2017.
 */
public class SubjectStore implements DatabaseStore {

    private static SubjectStore instance = null;

    private Map<Long, Subject> subjectIdToSubjectMap = new ConcurrentHashMap<>();
    private Map<Long, List<Subject>> schoolIdToSubjectListMap = new ConcurrentHashMap<>();

    public static SubjectStore getInstance(){

        if(instance == null){
            instance = new SubjectStore();
        }

        return instance;
    }


    public void loadFromDatabase(){

        try (Connection con = ConnectionPool.open()) {

            List<Subject> subjectList = SubjectDAO.getAll(con);

            for (Subject subject : subjectList) {

                List<Subject> subjectList1 = schoolIdToSubjectListMap.get(subject.getSchool_id());
                if(subjectList1 == null){
                    subjectList1 = new CopyOnWriteArrayList<>();
                    schoolIdToSubjectListMap.put(subject.getSchool_id(), subjectList1);
                }

                subjectList1.add(subject);
                subjectIdToSubjectMap.put(subject.getId(), subject);

            }


        }

    }

    @Override
    public void removeSchool(Long school_id) {

        for (Subject subject : schoolIdToSubjectListMap.get(school_id)) {
            subjectIdToSubjectMap.remove(subject.getId());
        }

        schoolIdToSubjectListMap.remove(school_id);

    }

    public List<Subject> getSubjectsForSchool(Long school_id){

        List<Subject> subjects = schoolIdToSubjectListMap.get(school_id);

        if(subjects ==  null){
            subjects = new CopyOnWriteArrayList<>();
            schoolIdToSubjectListMap.put(school_id, subjects);
        }

        return subjects;

    }

    public Subject getSubjectById(Long subject_id){
        return subjectIdToSubjectMap.get(subject_id);
    }

    public void removeSubject(Subject subject){
        List<Subject> subjectList =  schoolIdToSubjectListMap.get(subject.getSchool_id());
        if(subjectList != null){
            subjectList.remove(subject);
        }
        subjectIdToSubjectMap.remove(subject.getId());
    }

}
