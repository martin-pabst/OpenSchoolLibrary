package de.sp.modules.calendar.servlet;

import de.sp.database.model.*;
import de.sp.database.stores.StudentClassStore;
import de.sp.database.stores.UserRolePermissionStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 21.05.2017.
 */
public class GetEventDetailsResponse {

    public Event event;

    public List<EventDetailsResponseRestrictionEntry> userRestrictions = new ArrayList<>();
    public List<EventDetailsResponseRestrictionEntry> roleRestrictions = new ArrayList<>();
    



    public GetEventDetailsResponse(Event event, Long school_term_id){

        this.event = event;
        
        buildRestrictionLists(event);

        buildAbsentClassList(event, school_term_id);

        // List for ressources

        // Schulaufgaben, Exen, ...



    }

    private void buildAbsentClassList(Event event, Long school_term_id) {

        List<DBClass> classList = StudentClassStore.getInstance().getClassesInSchoolTerm(school_term_id);

        List<EventDetailsResponseTermEntry> termEntries = new ArrayList<>();
        Map<Long, EventDetailsResponseTermEntry> termEntryMap = new HashMap<>();


        for (DBClass dbClass : classList) {

            EventDetailsResponseTermEntry termEntry = termEntryMap.get(dbClass.getSchool_term_id());

            if(termEntry == null){

            }



        }




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
        
        for (EventRestriction eventRestriction : event.getRestrictions()) {
            
            if(eventRestriction.getUser_id() != null){

                Long user_id = eventRestriction.getUser_id();
                for (EventDetailsResponseRestrictionEntry userRestriction : userRestrictions) {
                    if(userRestriction.id.equals(user_id)){
                        userRestriction.isRestricted = true;
                        break;
                    }
                }

            }

            if(eventRestriction.getRole_id() != null){

                Long role_id = eventRestriction.getRole_id();
                for (EventDetailsResponseRestrictionEntry roleRestriction : roleRestrictions) {
                    if(roleRestriction.id.equals(role_id)){
                        roleRestriction.isRestricted = true;
                        break;
                    }
                }

            }
            
        }

    }


}
