package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.AbsenceDAO;
import de.sp.database.daos.basic.EventDAO;
import de.sp.database.daos.basic.EventRestrictionDAO;
import de.sp.database.model.Absence;
import de.sp.database.model.Event;
import de.sp.database.model.EventRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Martin on 09.05.2017.
 */
public class EventStore {

    private static EventStore instance;

    // maps 100 * year + month (january == 1) to Event objects
    private Map<Long, Map<Integer, List<Event>>> schoolIdToyearMonthToEventMap = new ConcurrentHashMap<>();

    private Map<Long, Map<Integer,List<Absence>>> classIdToYearMonthToAbsenceList = new ConcurrentHashMap<>();

    private Map<Long, Map<Integer,List<Absence>>> formIdToYearMonthToAbsenceList = new ConcurrentHashMap<>();

    private Map<Long, Map<Integer,List<Absence>>> schoolIdToYearMonthToAbsenceList = new ConcurrentHashMap<>();
    
    private Map<Long, Event> eventIdToEventMap = new ConcurrentHashMap<>();

    public static EventStore getInstance() {
        if (instance == null) {
            instance = new EventStore();
        }

        return instance;
    }


    /**
     * Store event with absences and restrictions into database and into EventStore.
     * @param event
     * @param con
     */
    public void storeEventIntoDatabase(Event event, Connection con){
        EventDAO.insert(event, con);
        addEvent(event);

        if(event.hasAbsences()){
            for (Absence absence : event.getAbsences()) {
                absence.setEvent_id(event.getId());
                AbsenceDAO.insert(absence, con);
                addAbsence(absence);
            }
        }

        if(event.hasRestrictions()){
            for (EventRestriction eventRestriction : event.getRestrictions()) {
                eventRestriction.setEvent_id(event.getId());
                EventRestrictionDAO.insert(eventRestriction, con);
            }
        }

    }




    private void addAbsence(Absence absence){
        Event event = eventIdToEventMap.get(absence.getEvent_id());
        absence.setEvent(event);
        event.addAbsence(absence);

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

    private void addEvent(Event event){

        eventIdToEventMap.put(event.getId(), event);

        List<Integer> yearMonthList = event.getYearMonthList();

        for (Integer yearMonthIndex : yearMonthList) {

            Map<Integer, List<Event>> yearMonthToEventMap = schoolIdToyearMonthToEventMap.get(event.getSchool_id());

            if(yearMonthToEventMap == null){
                yearMonthToEventMap = new ConcurrentHashMap<>();
                schoolIdToyearMonthToEventMap.put(event.getSchool_id(), yearMonthToEventMap);
            }

            List<Event> eventList = yearMonthToEventMap.get(yearMonthIndex);

            if(eventList == null){
                eventList = new CopyOnWriteArrayList<>();
                yearMonthToEventMap.put(yearMonthIndex, eventList);
            }

            eventList.add(event);

        }

    }

    public List<Event> getAbsencesForWholeSchool(Long school_id, Date from, Date to){
       
        return getAbsences(schoolIdToYearMonthToAbsenceList, school_id, from, to);
        
    }

    public List<Event> getAbsencesForClass(Long class_id, Date from, Date to){

        return getAbsences(classIdToYearMonthToAbsenceList, class_id, from, to);

    }

    public List<Event> getAbsencesForForm(Long form_id, Date from, Date to){

        return getAbsences(formIdToYearMonthToAbsenceList, form_id, from, to);

    }

    private List<Event> getAbsences(Map<Long, Map<Integer,List<Absence>>> idToYearMonthToAbsenceList, Long id, Date from, Date to){

        List<Integer> yearMonthIndexList = Event.getYearMonthList(from, to);

        List<Event> events = new ArrayList<>();

        Map<Integer,List<Absence>> yearMonthToAbsenceList = idToYearMonthToAbsenceList.get(id);
        
        if(yearMonthToAbsenceList != null) {
            for (Integer ym : yearMonthIndexList) {
                
                List<Absence> absenceList = yearMonthToAbsenceList.get(ym);
                
                if(absenceList != null){

                    for (Absence absence : absenceList) {
                        events.add(absence.getEvent());
                    }
                    
                }

            }
        }
        
        return events;
    }

    public List<Event> getEventRecords(Long school_id, Date from, Date to){

        List<Integer> yearMonthIndexList = Event.getYearMonthList(from, to);

        List<Event> events = new ArrayList<>();

        Map<Integer, List<Event>> yearMonthToEventMap = schoolIdToyearMonthToEventMap.get(school_id);

        if (yearMonthToEventMap != null) {
            for (Integer ym : yearMonthIndexList) {
                List<Event> cl = yearMonthToEventMap.get(ym);
                if(cl != null){
                    events.addAll(cl);
                }
            }
        }

        return events;

    }

    public void loadFromDatabase() {

        try (Connection con = ConnectionPool.open()) {

            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.info("Reading event and absences from database...");

            List<Event> events = EventDAO.getAllWithoutDescriptions(con);
            List<EventRestriction> restrictions = EventRestrictionDAO.getAll(con);
            List<Absence> absences = AbsenceDAO.getAll(con);

            // fill schoolIdToyearMonthToEventMap
            for (Event event : events) {

                addEvent(event);

            }

            // weave restrictions into event objects
            for (EventRestriction restriction : restrictions) {

                Event event = eventIdToEventMap.get(restriction.getEvent_id());
                restriction.setEvent(event);

                event.addRestriction(restriction);

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

        for(Integer yearMonth: absence.getEvent().getYearMonthList()){
            List<Absence> absenceList = yearMonthToAbsenceList.get(yearMonth);
            if(absenceList == null){
                absenceList = new CopyOnWriteArrayList<>();
                yearMonthToAbsenceList.put(yearMonth, absenceList);
            }
            absenceList.add(absence);
        }

    }





}
