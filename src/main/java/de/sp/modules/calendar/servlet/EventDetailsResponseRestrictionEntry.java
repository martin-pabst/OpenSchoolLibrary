package de.sp.modules.calendar.servlet;

/**
 * Created by Martin on 21.05.2017.
 */
public class EventDetailsResponseRestrictionEntry {

        public Long id; // user.id or role.id
        public String name;
        public boolean isRestricted; // true, if reading the event is restricted to this user/role

    public EventDetailsResponseRestrictionEntry(Long id, String name, boolean isRestricted) {
        this.id = id;
        this.name = name;
        this.isRestricted = isRestricted;
    }

}
