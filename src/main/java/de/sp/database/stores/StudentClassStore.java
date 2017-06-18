package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.DBClassDAO;
import de.sp.database.daos.basic.LanguageskillDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.StudentSchoolTermDAO;
import de.sp.database.model.*;
import org.sql2o.Connection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Martin on 09.05.2017.
 */
public class StudentClassStore implements DatabaseStore {

    private static StudentClassStore instance;

    private Map<Long, Student> studentIdToStudentMap = new ConcurrentHashMap<>();

    private Map<Long, StudentSchoolTerm> studentSchoolTermIdToStudentSchoolTermMap = new ConcurrentHashMap<>();

    private Map<Long, List<StudentSchoolTerm>> classIdToStudentSchoolTermListMap = new ConcurrentHashMap<>();

    private Map<Long, List<Languageskill>> studentIdToLanguageSkillMap = new ConcurrentHashMap<>();

    private Map<Long, DBClass> classMap = new ConcurrentHashMap<>();

    private Map<Long, List<DBClass>> schoolTermIdToClassListMap = new ConcurrentHashMap<>();


    public static StudentClassStore getInstance() {
        if (instance == null) {
            instance = new StudentClassStore();
        }

        return instance;
    }

    public void loadFromDatabase() {

        try (Connection con = ConnectionPool.open()) {

            /**
             * Read student
             */
            List<Student> studentList = StudentDAO.getAll(con);

            for (Student student : studentList) {
                studentIdToStudentMap.put(student.getId(), student);
            }

            /**
             * Read class
             */
            List<DBClass> classList = DBClassDAO.getAll(con);

            for (DBClass dbClass : classList) {
                addClass(dbClass);
            }

            /**
             * Read student_school_term
             */
            List<StudentSchoolTerm> studentSchoolTermList = StudentSchoolTermDAO.getAll(con);

            for (StudentSchoolTerm studentSchoolTerm : studentSchoolTermList) {
                addStudentSchoolTerm(studentSchoolTerm);
            }

            /**
             * Read Languageskill
             */
            List<Languageskill> languageskillList = LanguageskillDAO.getAll(con);

            for (Languageskill languageskill : languageskillList) {
                addLanguageskill(languageskill);
            }


        }

    }

    @Override
    public void removeSchool(Long school_id) {

        List<Student> students = new ArrayList<>();
        Set<Long> studentIdSet = new HashSet<>();

        studentIdToStudentMap.forEach((id, student) -> {
            if (school_id.equals(student.getSchool_id())) {
                students.add(student);
                studentIdSet.add(student.getId());
            }
        });

        for (Student student : students) {
            studentIdToStudentMap.remove(student.getId());
            studentIdToLanguageSkillMap.remove(student.getId());
        }

        for (SchoolTerm schoolTerm : SchoolTermStore.getInstance().getSchoolTerms(school_id)) {
            for (DBClass dbClass : schoolTermIdToClassListMap.get(schoolTerm.getId())) {
                classIdToStudentSchoolTermListMap.remove(dbClass.getId());
            }
            schoolTermIdToClassListMap.remove(schoolTerm.getId());
        }

        List<Long> studentSchoolTermIds = new ArrayList<>();

        studentSchoolTermIdToStudentSchoolTermMap.forEach((id, studentSchoolTerm) -> {
            if(studentIdSet.contains(id)){
                studentSchoolTermIds.add(id);
            }
        });

        for (Long id : studentSchoolTermIds) {
            studentSchoolTermIdToStudentSchoolTermMap.remove(id);
        }


    }

    private void addLanguageskill(Languageskill languageskill) {

        List<Languageskill>languageskillList = studentIdToLanguageSkillMap.get(languageskill.getStudent_id());

        if(languageskillList == null){
            languageskillList = new CopyOnWriteArrayList<>();
            studentIdToLanguageSkillMap.put(languageskill.getStudent_id(), languageskillList);
        }

        languageskillList.add(languageskill);

    }

    private void addStudentSchoolTerm(StudentSchoolTerm studentSchoolTerm) {

        studentSchoolTermIdToStudentSchoolTermMap.put(studentSchoolTerm.getId(), studentSchoolTerm);

        if(studentSchoolTerm.getClass_id() != null){

            List<StudentSchoolTerm> studentSchoolTermList = classIdToStudentSchoolTermListMap.get(studentSchoolTerm.getClass_id());

            if(studentSchoolTermList == null){
                studentSchoolTermList = new CopyOnWriteArrayList<>();
                classIdToStudentSchoolTermListMap.put(studentSchoolTerm.getClass_id(), studentSchoolTermList);
            }

            studentSchoolTermList.add(studentSchoolTerm);

        }

    }

    private void addClass(DBClass dbClass){

        classMap.put(dbClass.getId(), dbClass);

        List<DBClass> classList = schoolTermIdToClassListMap.get(dbClass.getSchool_term_id());

        if(classList == null){
            classList = new CopyOnWriteArrayList<>();
            schoolTermIdToClassListMap.put(dbClass.getSchool_term_id(), classList);
        }

        classList.add(dbClass);

    }

    public Student getStudentById(Long student_id){
        return studentIdToStudentMap.get(student_id);
    }

    public DBClass getClassById(Long class_id){
        return classMap.get(class_id);
    }

    public StudentSchoolTerm getStudentSchoolTermById(Long student_school_term_id){
        return studentSchoolTermIdToStudentSchoolTermMap.get(student_school_term_id);
    }

    public List<Languageskill> getLanguageSkillForStudent(Long student_id){
        return studentIdToLanguageSkillMap.get(student_id);
    }

    public List<StudentSchoolTerm> getStudentsInClass(Long class_id){
        return classIdToStudentSchoolTermListMap.get(class_id);
    }

    public List<DBClass> getClassesInSchoolTerm(Long school_term_id){
        return schoolTermIdToClassListMap.get(school_term_id);
    }

    public void removeStudent(Long student_id){

        List<StudentSchoolTerm> studentSchoolTermList = new ArrayList<>();

        studentSchoolTermIdToStudentSchoolTermMap.forEach((id, studentSchoolTerm) -> {
            studentSchoolTermList.add(studentSchoolTerm);
        });

        for (StudentSchoolTerm studentSchoolTerm : studentSchoolTermList) {
            removeStudentSchoolTerm(studentSchoolTerm);
        }

        studentIdToLanguageSkillMap.remove(student_id);

        studentIdToStudentMap.remove(student_id);

    }

    private void removeStudentSchoolTerm(StudentSchoolTerm studentSchoolTerm) {

        for (DBClass dbClass : schoolTermIdToClassListMap.get(studentSchoolTerm.getSchool_term_id())) {

            List<StudentSchoolTerm> studentSchoolTermList = classIdToStudentSchoolTermListMap.get(dbClass.getId());

            if(studentSchoolTermList != null){
                studentSchoolTermList.remove(studentSchoolTerm);
            }

        }

    }

    public void removeClass(Long class_id){
        DBClass dbClass = classMap.get(class_id);
        if(dbClass != null){
            List<DBClass> classList = schoolTermIdToClassListMap.get(dbClass.getSchool_term_id());
            if(classList != null){
                classList.remove(dbClass);
            }
        }

        classMap.remove(dbClass);
    }

}
