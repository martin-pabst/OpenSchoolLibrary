package de.sp.modules.library.servlets.reports.reportsschueler.neededbooks;

import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.model.Languageskill;
import de.sp.database.model.Subject;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.borrow.bookformstore.BookFormStoreRecord;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import org.sql2o.Connection;

import java.util.*;

/**
 * Created by Martin on 24.04.2017.
 */
public class NeededBooksHelper {

    private boolean schuelerOhneBenoetigteBuecherWeglassen;
    private Connection con;
    private Long school_id;
    private List<BookFormStoreRecord> bookFormStore;
    private List<Subject> subjects;
    private Map<Long, Subject> subjectMap = new HashMap<>();


    public NeededBooksHelper(Long school_id, Connection con, boolean schuelerOhneBenoetigteBuecherWeglassen) {
        this.school_id = school_id;
        this.con = con;
        this.schuelerOhneBenoetigteBuecherWeglassen = schuelerOhneBenoetigteBuecherWeglassen;
        init();
    }

    public void init(){

        bookFormStore = LibraryDAO.getBookFormStore(school_id, con);
        subjects = SubjectDAO.getAll(con, school_id);

        subjects.forEach(subject -> subjectMap.put(subject.getId(), subject));


    }

    public List<NeededBookRecord> getNeededBooks(BorrowerRecord br, HashSet<Long> borrowedBooksIds) {

        boolean mindestensEinBenoetigtesBuch = false;

        ArrayList<NeededBookRecord> neededBooks = new ArrayList<>();

        for (BookFormStoreRecord bookFormStoreRecord : bookFormStore) {

            Subject subject = subjectMap.get(bookFormStoreRecord.getSubject_id());

            if (bookFormStoreRecord.getForm_id() != null && !bookFormStoreRecord.getForm_id().equals(br.getForm_id())) {
                continue;
            }


            if (subject != null) {
                if (subject.is_religion()) {
                    if (br.getReligion_id() != null && !br.getReligion_id().equals(subject.getId())) {
                        continue;
                    }
                }

                if(br.getCurriculum_id() != null && bookFormStoreRecord.getCurriculum_id() != null){
                    if(!br.getCurriculum_id().equals(bookFormStoreRecord.getCurriculum_id())){
                        continue;
                    }
                }

                if (bookFormStoreRecord.getLanguageyear() != null) {

                    boolean ok = false;

                    for (Languageskill languageskill : br.getLanguageskills()) {

                        if (bookFormStoreRecord.getSubject_id() == null || !bookFormStoreRecord.getSubject_id().equals(languageskill.getSubject_id())) {
                            continue;
                        }

                        if (br.getYear_of_school() - languageskill.getFrom_year() + 1 == bookFormStoreRecord.getLanguageyear()) {
                            ok = true;
                            break;
                        }

                    }

                    if (!ok) {
                        continue;
                    }


                }
            }

            if (borrowedBooksIds.contains(bookFormStoreRecord.getBook_id())) {
                continue;
            }

            neededBooks.add(new NeededBookRecord(br, br.getClass_name(), br.getClass_id(),
                    br.getName(), br.getStudent_id(), bookFormStoreRecord.getSubject(), bookFormStoreRecord.getTitle(),
                    bookFormStoreRecord.getBook_id()));

            mindestensEinBenoetigtesBuch = true;
        }

        if(!mindestensEinBenoetigtesBuch && !schuelerOhneBenoetigteBuecherWeglassen){
            neededBooks.add(new NeededBookRecord(br, br.getClass_name(), br.getClass_id(),
                    br.getName(), br.getStudent_id(), null, null,
                    null));
        }

        return neededBooks;

    }
}
