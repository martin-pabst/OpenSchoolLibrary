package de.sp.modules.calendar.servlet;

import java.util.ArrayList;

/**
 * Created by Martin on 21.05.2017.
 */
public class EventDetailsResponseTermEntry {

    public Long term_id;
    public String term_name;

    public ArrayList<EventDetailsResponseClassEntry> classEntries = new ArrayList<>();

    public EventDetailsResponseTermEntry(Long term_id, String term_name) {
        this.term_id = term_id;
        this.term_name = term_name;
    }


}
