package de.sp.modules.library.servlets.inventory.books;

import de.sp.database.daos.basic.BookDAO;
import de.sp.database.model.Book;
import de.sp.modules.library.LibraryModule;
import de.sp.protocols.w2ui.grid.gridrequest.*;
import org.sql2o.Connection;

import java.util.List;
import java.util.Map;

public class LibraryInventoryBooksServlet extends
        BaseGridServlet<LibraryInventoryRecord> {

    @Override
    protected GridResponseUpdateDelete doDelete(GridRequestDelete deleteData,
                                                Connection con) throws Exception {

        for (Long id : deleteData.getSelected()) {

            if (!deleteData.getSchool_id().equals(BookDAO.getSchoolId(id, con))) {
                throw new Exception("Book with id " + id
                        + " does not belong to school with id "
                        + deleteData.getSchool_id());
            }

            BookDAO.delete(id, con);
        }

        return new GridResponseUpdateDelete(GridResponseStatus.success, "");
    }

    @Override
    protected GridResponseSave doSave(GridRequestSave saveData, Connection con)
            throws Exception {

        Map<String, Object> record = saveData.getRecord();

        Long subjectID = null;

        try {
            @SuppressWarnings("rawtypes")
            Map subject = (Map) record.get("subject");
            if (subject != null) {
                Object subjectIDo = subject.get("id");
                if (subjectIDo != null && !subjectIDo.toString().isEmpty()) {
                    Double d = Double.parseDouble(subjectIDo.toString());
                    subjectID = (long) d.intValue();
                }
            }
        } catch (Exception ex) {

        }

        Double price = null;
        Object priceO = record.get("price");
        if (priceO != null && !priceO.toString().isEmpty()) {
            price = Double.parseDouble(priceO.toString());
        }

        Book book = BookDAO.insert(saveData.getSchool_id(),
                saveCastToString(record.get("title")),
                saveCastToString(record.get("author")),
                saveCastToString(record.get("isbn")),
                saveCastToString(record.get("publisher")),
                saveCastToString(record.get("remarks")),
                saveCastToString(record.get("approval_code")),
                saveCastToString(record.get("edition")),
                subjectID, price, con);

        return new GridResponseSave(GridResponseStatus.success, "",
                book.getId());

    }

    @Override
    protected GridResponseGet<LibraryInventoryRecord> doGet(
            GridRequestGet getData, Connection con) {

        List<LibraryInventoryRecord> records = LibraryInventoryDAO
                .getInventoryList(getData.getSchool_id(), getData.getLimit(),
                        getData.getOffset(), con);

        return new GridResponseGet<LibraryInventoryRecord>(
                GridResponseStatus.success, records.size(), records, "", null);

    }

    @Override
    protected GridResponseUpdateDelete doUpdate(GridRequestUpdate updateData,
                                                Connection con) throws Exception {

        List<Map<String, Object>> changes = updateData.getChanges();

        for (Map<String, Object> change : changes) {

            Long recid = (long) ((double) change.get("recid"));

            if (!updateData.getSchool_id().equals(
                    BookDAO.getSchoolId(recid, con))) {
                throw new Exception("Book with id " + recid
                        + " does not belong to school with id "
                        + updateData.getSchool_id());
            }

            String[] keys = change.keySet().toArray(new String[0]);

            for (String key : keys) {

                if (key.equals("recid")) {
                    continue;
                }

                update(updateData.getSchool_id(), con, key, change.get(key),
                        recid);

            }

        }

        return new GridResponseUpdateDelete(GridResponseStatus.success, "");
    }

    private void update(Long school_id, Connection con, String key,
                        Object value, Long recid) throws Exception {

        switch (key) {
            case "price":
                if (value != null && value.toString().isEmpty()) {
                    value = null;
                }
            case "title":
            case "author":
            case "publisher":
            case "approval_code":
            case "edition":
            case "isbn":
                String statement = "update book set " + key
                        + " = :value where id = :id";
                con.createQuery(statement).addParameter("value", value)
                        .addParameter("id", recid).executeUpdate();
                break;
            case "subject":
                @SuppressWarnings("rawtypes")
                Long subjectID = (long) ((double) ((Map) value).get("id"));

                if (subjectID != null) {
                    String statement2 = "update book set subject_id = :subject_id "
                            + "where id = :id";
                    con.createQuery(statement2)
                            .addParameter("subject_id", subjectID)
                            .addParameter("id", recid).executeUpdate();
                } else {
                    throw new Exception("Zur Fachabkürzung " + value.toString()
                            + " gibt es mehrere Einträge in der Fächerliste.");
                }

                break;

        }

    }

    @Override
    protected String getRequiredPermission(String command) {
        switch (command) {
            case "get":
                return LibraryModule.PERMISSION_LIBRARY;
            case "update":
                return LibraryModule.PERMISSION_INVENTORY;
            case "save":
                return LibraryModule.PERMISSION_INVENTORY;
            case "delete":
                return LibraryModule.PERMISSION_INVENTORY;

            default:
                return LibraryModule.PERMISSION_LIBRARY;
        }
    }
}
