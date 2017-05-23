package de.sp.modules.calendar.servlet;

/**
 * Created by Martin on 21.05.2017.
 */
public class EventDetailsResponseClassEntry {

    public long class_id;
    public String class_name;
    public boolean is_absent;

    public EventDetailsResponseClassEntry(long class_id, String class_name, boolean is_absent) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.is_absent = is_absent;
    }
}
