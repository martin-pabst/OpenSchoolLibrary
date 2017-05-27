package de.sp.modules.calendar.servlet;

import de.sp.tools.string.FormTool;

/**
 * Created by Martin on 21.05.2017.
 */
public class EventDetailsResponseClassEntry implements Comparable<EventDetailsResponseClassEntry> {

    public long class_id;
    public String class_name;
    public boolean is_absent;

    public EventDetailsResponseClassEntry(long class_id, String class_name, boolean is_absent) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.is_absent = is_absent;
    }

    @Override
    public int compareTo(EventDetailsResponseClassEntry o) {
        Integer i1 = FormTool.formToInteger(class_name);
        Integer i2 = FormTool.formToInteger(o.class_name);
        return i1.compareTo(i2);
    }
}
