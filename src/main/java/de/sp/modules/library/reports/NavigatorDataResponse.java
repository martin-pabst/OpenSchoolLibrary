package de.sp.modules.library.reports;

import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.modules.library.servlets.inventory.books.LibraryInventoryRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 17.04.2017.
 */
public class NavigatorDataResponse {

    public List<BorrowerRecord> schueler;
    public List<BorrowerRecord> lehrer;
    public List<ClassRecord> klassen;
    public List<LibraryInventoryRecord> buecher;

    public String reports = "xx";

    public void extractLehrerFromSchuelerList() {

        if(schueler != null){
            lehrer = new ArrayList<>();

            int i = 0;

            while(i < schueler.size()){
                BorrowerRecord br = schueler.get(i);
                if(br.getTeacher_id() != null){
                    lehrer.add(br);
                    schueler.remove(i);
                } else {
                    i++;
                }
            }

        }

    }
}
