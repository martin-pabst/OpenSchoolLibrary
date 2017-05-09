package de.sp.database.stores;

import de.sp.database.model.Absence;
import de.sp.database.model.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 09.05.2017.
 */
public class CalendarStore {

    // maps 100 * year + month (january == 1) to Calendar objects
    private Map<Integer, List<Calendar>> yearMonthToCalendarMap = new HashMap<>();

    private Map<Long, List<Absence>> classIdToAbsenceMap = new HashMap<>();

    private Map<Long, List<Absence>> formIdToAbsenceMap = new HashMap<>();






}
