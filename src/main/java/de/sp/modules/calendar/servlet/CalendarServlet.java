package de.sp.modules.calendar.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.EventDAO;
import de.sp.database.model.*;
import de.sp.database.stores.EventStore;
import de.sp.database.stores.StudentClassStore;
import de.sp.database.stores.ValueListStore;
import de.sp.database.valuelists.ValueListType;
import de.sp.main.resources.modules.InsufficientPermissionException;
import de.sp.main.resources.text.TS;
import de.sp.modules.calendar.CalendarModule;
import de.sp.modules.library.servlets.settings.DeleteOldRecordsResponse;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm").create();

        String responseString = "";

        String command = getLastURLPart(request);

        try (Connection con = ConnectionPool.beginTransaction()) {

            try {

                switch (command) {

                    case "fetchEntries":

                        //school_id=1&type=schedule&start=2017-05-01&end=2017-06-12

                        Map<String, String> parameters = decodePostParameters(postData);

                        GetEventRequest gcr = new GetEventRequest(parameters.get("school_id"), parameters.get("type"),
                                parameters.get("start"), parameters.get("end"));

                        gcr.validate(ts);

                        user.checkPermission(CalendarModule.CALENDAROPEN,
                                gcr.school_id);

                        List<Event> gcResponse = fetchCalendarEntries(con, gcr, user);

                        Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        responseString = gson1.toJson(gcResponse);

                        break;

                    case "fetchEventDetails":

                        GetEventDetailsRequest gedr = gson.fromJson(postData, GetEventDetailsRequest.class);

                        gedr.validate(ts);

                        user.checkPermission(CalendarModule.CALENDAROPEN,
                                gedr.school_id);

                        responseString = gson.toJson(getEventDetailsResponse(gedr, user, con));

                        break;

                    case "setEventDetails": // also inserts new Events if given id == null

                        SetEventDetailsRequest sedr = gson.fromJson(postData, SetEventDetailsRequest.class);

                        sedr.validate(ts);
                        user.checkPermission(CalendarModule.CALENDAROPEN,
                                sedr.school_id);

                        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        responseString = gson.toJson(setEventDetails(sedr, user, con));

                        break;

                    case "removeEvent":

                        RemoveEventRequest rer = gson.fromJson(postData, RemoveEventRequest.class);

                        rer.validate(ts);

                        user.checkPermission(CalendarModule.CALENDAROPEN,
                                rer.school_id);

                        responseString = gson.toJson(removeEvent(rer, user, con));

                        break;

                    case "moveEvent":
                        MoveEventRequest mer = gson.fromJson(postData, MoveEventRequest.class);

                        mer.validate(ts);

                        user.checkPermission(CalendarModule.CALENDAROPEN,
                                mer.school_id);

                        responseString = gson.toJson(moveEvent(mer, user, con));

                        break;


                }

                con.commit(true);

            } catch (Exception ex) {
                logger.error("Error serving Request", ex);
                con.rollback();
                responseString = gson.toJson(new DeleteOldRecordsResponse("error", "Fehler: " + ex.toString()));
            }

        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(responseString);

    }

    private Object removeEvent(RemoveEventRequest rer, User user, Connection con) throws Exception {

        Event event = EventStore.getInstance().getEventById(rer.event_id);

        if(!event.getSchool_id().equals(rer.school_id)){
            return new RemoveEventResponse("error", "Dieser Termin gehört zu einer anderen Schule und kann daher nicht gelöscht werden.");
        }

        EventStore.getInstance().removeEventWithAbsencesAndRestrictionsFromStoreAndDatabase(event, con);

        return new RemoveEventResponse("success", "");
    }

    private SetEventDetailsResponse setEventDetails(SetEventDetailsRequest sedr, User user, Connection con) throws Exception {

        Event event;

        if(sedr.backgroundRendering){
            sedr.allDay = true;
        }

        EventStore eventStore = EventStore.getInstance();

        if(sedr.id == null){

            event = new Event(sedr.school_id, sedr.title, sedr.description, sedr.short_title,
                    sedr.location, sedr.allDay, sedr.preliminary, sedr.start, sedr.end,
                    sedr.backgroundColor,
                    sedr.borderColor, sedr.textColor, sedr.backgroundRendering);

        } else {

            event = eventStore.getEventById(sedr.id);

            if(!event.getSchool_id().equals(sedr.school_id)){
                throw new Exception("Der Termin gehört zu einer anderen Schule als der Benutzer, der ihn verändern möchte.");
            }

            eventStore.removeAbsencesAndRestrictionsFromStoreAndDatabase(event, con);
            eventStore.removeEventWithoutAbsencesFromStore(event);

            event.updateAttributes(sedr.title, sedr.description, sedr.short_title,
                    sedr.location, sedr.allDay, sedr.preliminary, sedr.start, sedr.end,
                    sedr.backgroundColor,
                    sedr.borderColor, sedr.textColor, sedr.backgroundRendering);

            EventDAO.update(event, con);

        }
        
        addAbsentClassesToEvent(sedr, event);

        addRestrictionsToEvent(sedr, event);

        eventStore.storeEventWithAbsencesAndRestrictionsIntoDatabaseAndStore(event, con, sedr.id != null);

        event.setEditable(true);

        return new SetEventDetailsResponse("success", "", event);

    }

    private void addRestrictionsToEvent(SetEventDetailsRequest sedr, Event event) {

        //create new restrictions
        //if user clicks on "visible for whole school" then -1 is in list
        if(!sedr.restrictionIndices.contains(new Long(-1))) {
            for (Long role_id : sedr.restrictionIndices) {
                EventRestriction eventRestriction = new EventRestriction(role_id, event);
                event.getRestrictions().add(eventRestriction);
            }
        }

    }

    private void addAbsentClassesToEvent(SetEventDetailsRequest sedr, Event event) {

        if(sedr.absenceWholeSchool){
            // this leads to all stored absences for classes/forms being removed later on:
            sedr.absencesSelectedClasses.clear();

            Absence absence = new Absence(sedr.school_id, null, null, sedr.absenceNoBigTests, sedr.absenceNoSmallTests);
            event.addAbsence(absence);

        }

        // if all classes of one form are absent, then remove them out of sedr.absentClassIds and add form_id in this List:
        List<Long> absent_form_ids = getAbsentForms(sedr);

        //Insert new absence-entries
        for (Long absent_form_id : absent_form_ids) {

            Absence absence = new Absence(null, null, absent_form_id, sedr.absenceNoBigTests, sedr.absenceNoSmallTests);
            event.addAbsence(absence);

        }

        for (Long class_id : sedr.absencesSelectedClasses) {

            Absence absence = new Absence(null, class_id, null, sedr.absenceNoBigTests, sedr.absenceNoSmallTests);
            event.addAbsence(absence);

        }

    }

    private List<Long> getAbsentForms(SetEventDetailsRequest sedr) {
        
        ArrayList<Long> absent_form_ids = new ArrayList<>();
        
        List<DBClass> classList = StudentClassStore.getInstance().
                getClassesInSchoolTerm(sedr.school_term_id);

        List<Value> forms = ValueListStore.getInstance().getValueList(sedr.school_id, ValueListType.form.getKey());

        for (Value form : forms) {
        
            boolean allClassesInForm = false;
            List<Long> classIds = new ArrayList<>();

            // if at leas one class of this school belongs to given form and ALL classes of given school are marked
            // absent in GUI (so that the belong to sedr.absencesSelectedClasses) then allClassesInForm == true
            for (DBClass dbClass : classList) {
                if(form.getId().equals(dbClass.getForm_id())){
                    allClassesInForm = true;  // at least one class of this school belongs to given form
                    classIds.add(dbClass.getId());
                    if(!sedr.absencesSelectedClasses.contains(dbClass.getId())){
                        allClassesInForm = false;
                        break;
                    }
                }
            }
            
            if(allClassesInForm){
                absent_form_ids.add(form.getId());
                sedr.absencesSelectedClasses.removeAll(classIds);
            }
            
            classIds.clear();
            
        }
        
        return absent_form_ids;

    }

    private GetEventDetailsResponse getEventDetailsResponse(GetEventDetailsRequest gedr, User user, Connection con) throws InsufficientPermissionException {

        Event event = null;

        if(gedr.event_id != null) {
            event = EventStore.getInstance().getEventById(gedr.event_id);
            EventDAO.addDescription(event, con);

            if(!checkIfUserMayReadEvent(user, event)){
                throw new InsufficientPermissionException("User must not read this event.");
            }
        }

        GetEventDetailsResponse response = new GetEventDetailsResponse(event, gedr.school_id, gedr.school_term_id);

        return response;

    }

    private List<Event> fetchCalendarEntries(Connection con, GetEventRequest gcr, User user) {

        List<Event> eventEntries = EventStore.getInstance().getEventRecords(gcr.school_id, gcr.start, gcr.end);

        filterByType(eventEntries, gcr.type); // filter schedule, absences or tests

        applyRestrictions(user, eventEntries); // if restriction is set, show only entries which are allowed

        // TODO: only user with permission ... may edit entries
        for (Event eventEntry : eventEntries) {
            eventEntry.setEditable(true);
        }

        return eventEntries;

    }

    private void filterByType(List<Event> eventEntries, String type) throws IllegalArgumentException {

        // throws IllegalArgumentException if value of GetCalendarRequestType with given name does not exist.

        GetCalendarRequestType rt = GetCalendarRequestType.valueOf(type);

        int i = 0;

        while (i < eventEntries.size()) {

            Event entry = eventEntries.get(i);

            boolean removeEntry = false;

            switch (rt) {
                case schedule:
                    removeEntry = (entry.isTest());
                    break;
                case absences:
                    removeEntry = (!entry.hasAbsences());
                    break;
                case tests:
                    removeEntry = (!entry.isTest());
                    break;
            }

            if (removeEntry) {
                eventEntries.remove(i);
            } else {
                i++;
            }
        }
    }

    private void applyRestrictions(User user, List<Event> eventEntries) {
        // Check for Restrictions
        int i = 0;

        while (i < eventEntries.size()) {

            Event entry = eventEntries.get(i);

            boolean keepEntry = checkIfUserMayReadEvent(user, entry);

            if (!keepEntry) {
                eventEntries.remove(i);
            } else {
                i++;
            }

        }
    }

    private boolean checkIfUserMayReadEvent(User user, Event entry) {
        boolean keepEntry = true;

        if (entry.hasRestrictions()) {

            keepEntry = false;

            for (EventRestriction cr : entry.getRestrictions()) {
                if (cr.getRole_id() != null && user.hasRole(cr.getRole_id())) {
                    keepEntry = true;
                    break;
                }
            }

        }
        return keepEntry;
    }

    private Object moveEvent(MoveEventRequest mer, User user, Connection con) throws Exception {

        Event event = EventStore.getInstance().getEventById(mer.id);

        if(event == null || !event.getSchool_id().equals(mer.school_id)){
            return new RemoveEventResponse("error", "Dieser Termin gehört zu einer anderen Schule und kann daher nicht gelöscht werden.");
        }

        EventStore.getInstance().moveEventInStore(event, mer.start, mer.end);

        event.setAllDay(mer.allDay);

        EventDAO.update(event, con);

        return new MoveEventResponse("success", "");
    }


}
