package de.sp.modules.calendar.servlet;

import de.sp.tools.string.FormTool;

import java.util.ArrayList;

/**
 * Created by Martin on 21.05.2017.
 */
public class EventDetailsResponseFormEntry implements Comparable<EventDetailsResponseFormEntry> {

    public Long form_id;
    public String form_name;
    public boolean is_absent;

    public ArrayList<EventDetailsResponseClassEntry> classEntries = new ArrayList<>();

    public EventDetailsResponseFormEntry(Long form_id, String form_name, boolean is_absent) {
        this.form_id = form_id;
        this.form_name = form_name;
        this.is_absent = is_absent;
    }

    @Override
    public int compareTo(EventDetailsResponseFormEntry o) {
        Integer i1 = FormTool.formToInteger(form_name);
        Integer i2 = FormTool.formToInteger(o.form_name);
        return i1.compareTo(i2);
    }
}
