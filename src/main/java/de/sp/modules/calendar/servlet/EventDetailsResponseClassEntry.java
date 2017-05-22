package de.sp.modules.calendar.servlet;

/**
 * Created by Martin on 21.05.2017.
 */
public class EventDetailsResponseClassEntry {

    public int class_id;
    public int class_name;
    public boolean is_absent;

    public EventDetailsResponseClassEntry(int class_id, int class_name, boolean is_absent) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.is_absent = is_absent;
    }
}
