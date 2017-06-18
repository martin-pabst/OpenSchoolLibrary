package de.sp.database.model;

/**
 * Created by Martin on 17.06.2017.
 */
public interface DatabaseStore {

    public void loadFromDatabase();

    public void removeSchool(Long school_id);


}
