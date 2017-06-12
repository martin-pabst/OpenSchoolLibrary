package de.sp.modules.calendar.servlet;

import de.sp.database.model.*;
import de.sp.database.stores.StudentClassStore;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.database.stores.ValueListStore;
import de.sp.database.valuelists.ValueListType;

import java.util.*;

/**
 * Created by Martin on 21.05.2017.
 */
public class GetEventDetailsResponse {

    public Event event;

    public List<EventDetailsResponseRestrictionEntry> roleRestrictions = new ArrayList<>();
    public List<EventDetailsResponseFormEntry> formEntries = new ArrayList<>();

    public Boolean absenceWholeSchool = false;


    public GetEventDetailsResponse(Event event, Long school_id, Long school_term_id){

        this.event = event;
        
        buildRestrictionLists(event, school_id);

        buildAbsentClassList(event, school_id, school_term_id);

        absenceWholeSchool = event == null ? false : event.wholeSchoolIsAbsent();

        // List of ressources

        // Schulaufgaben, Exen, ...



    }

    private void buildAbsentClassList(Event event, Long school_id, Long school_term_id) {

        List<DBClass> classList = StudentClassStore.getInstance().getClassesInSchoolTerm(school_term_id);

        formEntries.clear();
        Map<Long, EventDetailsResponseFormEntry> formEntryMap = new HashMap<>();


        for (DBClass dbClass : classList) {

            Long form_id = dbClass.getForm_id();

            if(form_id != null) {

                EventDetailsResponseFormEntry formEntry = formEntryMap.get(form_id);

                if (formEntry == null) {

                    boolean formIsAbsent = event == null ? false : event.formIsAbsent(form_id);

                    Value form = ValueListStore.getInstance().getValue(school_id, ValueListType.form.getKey(), form_id);
                    formEntry = new EventDetailsResponseFormEntry(form_id, form.getName(), formIsAbsent);
                    formEntryMap.put(form_id, formEntry);

                    formEntries.add(formEntry);

                }

                boolean classIsAbsent = event == null ? false : event.classIsAbsent(dbClass.getId()) || formEntry.is_absent;

                EventDetailsResponseClassEntry classEntry = new EventDetailsResponseClassEntry(dbClass.getId(),
                        dbClass.getName(), classIsAbsent);

                formEntry.classEntries.add(classEntry);

            }

        }

        Collections.sort(formEntries);

        formEntries.forEach(fe -> Collections.sort(fe.classEntries));

    }

    private void buildRestrictionLists(Event event, Long school_id) {
        
        UserRolePermissionStore ups = UserRolePermissionStore.getInstance();
        
        List<Role> roles = ups.getRoleListBySchoolId(school_id);

        for (Role role : roles) {
            EventDetailsResponseRestrictionEntry edre = new EventDetailsResponseRestrictionEntry(role.getId(), role.getName(), false);
            roleRestrictions.add(edre);
        }

        if(event != null && event.getRestrictions() != null) {
            for (EventRestriction eventRestriction : event.getRestrictions()) {

                if (eventRestriction.getRole_id() != null) {

                    Long role_id = eventRestriction.getRole_id();
                    for (EventDetailsResponseRestrictionEntry roleRestriction : roleRestrictions) {
                        if (roleRestriction.id.equals(role_id)) {
                            roleRestriction.isRestricted = true;
                            break;
                        }
                    }

                }

            }

        }

    }


}
