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

    public List<EventDetailsResponseRestrictionEntry> userRestrictions = new ArrayList<>();
    public List<EventDetailsResponseRestrictionEntry> roleRestrictions = new ArrayList<>();
    public List<EventDetailsResponseFormEntry> formEntries = new ArrayList<>();

    public Boolean absenceWholeSchool = false;


    public GetEventDetailsResponse(Event event, Long school_id, Long school_term_id){

        this.event = event;
        
        buildRestrictionLists(event);

        buildAbsentClassList(event, school_id, school_term_id);

        absenceWholeSchool = event.wholeSchoolIsAbsent();

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

                    Value form = ValueListStore.getInstance().getValue(school_id, ValueListType.form.getKey(), form_id);
                    formEntry = new EventDetailsResponseFormEntry(form_id, form.getName(), event.formIsAbsent(form_id));
                    formEntryMap.put(form_id, formEntry);

                    formEntries.add(formEntry);

                }

                EventDetailsResponseClassEntry classEntry = new EventDetailsResponseClassEntry(dbClass.getId(),
                        dbClass.getName(), event.classIsAbsent(dbClass.getId()) || formEntry.is_absent);

                formEntry.classEntries.add(classEntry);

            }

        }

        Collections.sort(formEntries);

        formEntries.forEach(fe -> Collections.sort(fe.classEntries));

    }

    private void buildRestrictionLists(Event event) {
        
        UserRolePermissionStore ups = UserRolePermissionStore.getInstance();
        
        List<User> users = ups.getUserListBySchoolId(event.getSchool_id());
        List<Role> roles = ups.getRoleListBySchoolId(event.getSchool_id());

        for (User user : users) {
            EventDetailsResponseRestrictionEntry edre = new EventDetailsResponseRestrictionEntry(user.getId(), user.getUsername() + " (" + user.getName() + ")", false);
            userRestrictions.add(edre);
        }

        for (Role role : roles) {
            EventDetailsResponseRestrictionEntry edre = new EventDetailsResponseRestrictionEntry(role.getId(), role.getName(), false);
            roleRestrictions.add(edre);
        }

        if(event.getRestrictions() != null) {
            for (EventRestriction eventRestriction : event.getRestrictions()) {

                if (eventRestriction.getUser_id() != null) {

                    Long user_id = eventRestriction.getUser_id();
                    for (EventDetailsResponseRestrictionEntry userRestriction : userRestrictions) {
                        if (userRestriction.id.equals(user_id)) {
                            userRestriction.isRestricted = true;
                            break;
                        }
                    }

                }

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
