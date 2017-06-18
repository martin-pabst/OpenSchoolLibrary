package de.sp.modules.root.schooladministration;

import de.sp.database.model.School;
import de.sp.database.stores.SchoolTermStore;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 03.05.2017.
 */
public class SchoolAdministrationListsResponse {

    public List<SchoolData> schools;

    public SchoolAdministrationListsResponse(){}

    public SchoolAdministrationListsResponse(Connection con){

        schools = new ArrayList<>();

        for (School school : SchoolTermStore.getInstance().getSchools()) {

            schools.add(new SchoolData(school));

        }

    }

}
