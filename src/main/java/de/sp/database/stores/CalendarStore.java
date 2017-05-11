package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.AbsenceDAO;
import de.sp.database.daos.basic.CalendarDAO;
import de.sp.database.daos.basic.CalendarRestrictionDAO;
import de.sp.database.model.Absence;
import de.sp.database.model.Calendar;
import de.sp.database.model.CalendarRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Martin on 09.05.2017.
 */
public class CalendarStore {

    private static CalendarStore instance;

    // maps 100 * year + month (january == 1) to Calendar objects
    private Map<Integer, List<Calendar>> yearMonthToCalendarMap = new ConcurrentHashMap<>();

    private Map<Long, Map<Integer,List<Absence>>> classIdToYearMonthToAbsenceList = new ConcurrentHashMap<>();

    private Map<Long, Map<Integer,List<Absence>>> formIdToYearMonthToAbsenceList = new ConcurrentHashMap<>();

    private Map<Long, Map<Integer,List<Absence>>> schoolIdToYearMonthToAbsenceList = new ConcurrentHashMap<>();
    
    private Map<Long, Calendar> calendarIdToCalendarMap = new ConcurrentHashMap<>();

    public static CalendarStore getInstance() {
        if (instance == null) {
            instance = new CalendarStore();
        }

        return instance;
    }


    public void addAbsence(Absence absence){
        Calendar calendar = calendarIdToCalendarMap.get(absence.getCalendar_id());
        absence.setCalendar(calendar);
        calendar.addAbsence(absence);

        if(absence.getForm_id() != null){

            storeAbsence(formIdToYearMonthToAbsenceList, absence.getForm_id(), absence);

        }

        if(absence.getClass_id() != null){

            storeAbsence(classIdToYearMonthToAbsenceList, absence.getClass_id(), absence);

        }

        if(absence.getSchool_id() != null){

            storeAbsence(schoolIdToYearMonthToAbsenceList, absence.getSchool_id(), absence);

        }

    }

    public void addCalendar(Calendar calendar){

        calendarIdToCalendarMap.put(calendar.getId(), calendar);

        List<Integer> yearMonthList = calendar.getYearMonthList();

        for (Integer yearMonthIndex : yearMonthList) {

            List<Calendar> calendarList = yearMonthToCalendarMap.get(yearMonthIndex);

            if(calendarList == null){
                calendarList = new CopyOnWriteArrayList<>();
                yearMonthToCalendarMap.put(yearMonthIndex, calendarList);
            }

            calendarList.add(calendar);

        }

    }

    public List<Calendar> getAbsencesForWholeSchool(Long school_id, Date from, Date to){
       
        return getAbsences(schoolIdToYearMonthToAbsenceList, school_id, from, to);
        
    }

    public List<Calendar> getAbsencesForClass(Long class_id, Date from, Date to){

        return getAbsences(classIdToYearMonthToAbsenceList, class_id, from, to);

    }

    public List<Calendar> getAbsencesForForm(Long form_id, Date from, Date to){

        return getAbsences(formIdToYearMonthToAbsenceList, form_id, from, to);

    }

    private List<Calendar> getAbsences(Map<Long, Map<Integer,List<Absence>>> idToYearMonthToAbsenceList, Long id, Date from, Date to){

        List<Integer> yearMonthIndexList = Calendar.getYearMonthList(from, to);

        List<Calendar> calendars = new ArrayList<>();

        Map<Integer,List<Absence>> yearMonthToAbsenceList = idToYearMonthToAbsenceList.get(id);
        
        if(yearMonthToAbsenceList != null) {
            for (Integer ym : yearMonthIndexList) {
                
                List<Absence> absenceList = yearMonthToAbsenceList.get(ym);
                
                if(absenceList != null){

                    for (Absence absence : absenceList) {
                        calendars.add(absence.getCalendar());
                    }
                    
                }

            }
        }
        
        return calendars;
    }

    public List<Calendar> getCalendarRecords(Date from, Date to){

        List<Integer> yearMonthIndexList = Calendar.getYearMonthList(from, to);

        List<Calendar> calendars = new ArrayList<>();

        for (Integer ym : yearMonthIndexList) {
            List<Calendar> cl = yearMonthToCalendarMap.get(ym);
            if(cl != null){
                calendars.addAll(cl);
            }
        }

        return calendars;

    }

    public void loadFromDatabase() {

        try (Connection con = ConnectionPool.open()) {

            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.info("Reading calendar and absences from database...");

            List<Calendar> calendars = CalendarDAO.getAllWithoutDescriptions(con);
            List<CalendarRestriction> restrictions = CalendarRestrictionDAO.getAll(con);
            List<Absence> absences = AbsenceDAO.getAll(con);

            // fill yearMonthToCalendarMap
            for (Calendar calendar : calendars) {

                addCalendar(calendar);

            }

            // weave restrictions into calendar objects
            for (CalendarRestriction restriction : restrictions) {

                Calendar calendar = calendarIdToCalendarMap.get(restriction.getCalendar_id());
                restriction.setCalendar(calendar);

                calendar.addRestriction(restriction);

            }

            // fill absence maps
            for (Absence absence : absences) {

                addAbsence(absence);

            }

        }
    }
    
    private void storeAbsence(Map<Long, Map<Integer,List<Absence>>> idToYearMonthToAbsenceList, Long id, Absence absence){
       
        Map<Integer,List<Absence>> yearMonthToAbsenceList = idToYearMonthToAbsenceList.get(id);

        if(yearMonthToAbsenceList == null){
            yearMonthToAbsenceList = new ConcurrentHashMap<>();
            formIdToYearMonthToAbsenceList.put(id, yearMonthToAbsenceList);
        }

        for(Integer yearMonth: absence.getCalendar().getYearMonthList()){
            List<Absence> absenceList = yearMonthToAbsenceList.get(yearMonth);
            if(absenceList == null){
                absenceList = new CopyOnWriteArrayList<>();
                yearMonthToAbsenceList.put(yearMonth, absenceList);
            }
            absenceList.add(absence);
        }

    }


}
