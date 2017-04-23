package de.sp.modules.library.servlets.inventory.books;

import de.sp.database.daos.basic.BookDAO;
import de.sp.database.daos.basic.BookFormDAO;
import de.sp.database.model.BookForm;
import de.sp.modules.library.LibraryModule;
import de.sp.protocols.w2ui.grid.gridrequest.*;
import org.sql2o.Connection;

import java.util.List;
import java.util.Map;

public class LibraryBookFormServlet extends BaseGridServlet<Object> {

	@Override
	protected GridResponseUpdateDelete doUpdate(GridRequestUpdate requestData,
			Connection con) throws Exception {

		List<Map<String, Object>> changes = requestData.getChanges();

		for (Map<String, Object> change : changes) {

			Long recid = (long) ((double) change.get("recid"));

			if (!requestData.getSchool_id().equals(
					BookFormDAO.getSchoolId(recid, con))) {
				throw new Exception("BookForm-Entry with id " + recid
						+ " does not belong to school with id "
						+ requestData.getSchool_id());
			}

			String[] keys = change.keySet().toArray(new String[0]);

			for (String key : keys) {

				if (key.equals("recid")) {
					continue;
				}

				update(requestData.getSchool_id(), con, key, change.get(key),
						recid);

			}

		}

		return new GridResponseUpdateDelete(GridResponseStatus.success, "");
	}

	@SuppressWarnings("rawtypes")
	private void update(Long school_id, Connection con, String key,
			Object value, Long recid) throws Exception {

		String statement = "";

		switch (key) {
		case "form":
			Long formID = (long) ((double) ((Map) value).get("id"));

			statement = "update book_form set form_id = :form_id "
					+ "where id = :id";
			con.createQuery(statement).addParameter("form_id", formID)
					.addParameter("id", recid).executeUpdate();
			break;
		case "curriculum":
			Long curriculumID = null;
			try {
				if (!((Map) value).get("id").toString().equals("Alle")) {
					curriculumID = (long) ((double) ((Map) value).get("id"));
				}
			} catch(Exception ex){

			}

			statement = "update book_form set curriculum_id = :curriculum_id "
					+ "where id = :id";
			con.createQuery(statement)
					.addParameter("curriculum_id", curriculumID)
					.addParameter("id", recid).executeUpdate();
			break;
		case "languageyear":

			Integer languageyear = null;

			if (value != null && !value.toString().isEmpty()) {
				languageyear = (int) ((double) value);
			}

			statement = "update book_form set languageyear = :languageyear "
					+ "where id = :id";
			con.createQuery(statement)
					.addParameter("languageyear", languageyear)
					.addParameter("id", recid).executeUpdate();
			break;

		}

	}

	@Override
	protected GridResponseSave doSave(GridRequestSave requestData,
			Connection con) throws Exception {

		Map<String, Object> record = requestData.getRecord();

		Long form_id = null;

		try {
			@SuppressWarnings("rawtypes")
			Map form = (Map) record.get("form");
			if (form != null) {
				Object formIDo = form.get("id");
				if (formIDo != null && !formIDo.toString().isEmpty()) {
					Double d = Double.parseDouble(formIDo.toString());
					form_id = (long) d.intValue();
				}
			}
		} catch (Exception ex) {

		}

		Long curriculum_id = null;

		try {
			@SuppressWarnings("rawtypes")
			Map curriculum = (Map) record.get("curriculum");
			if (curriculum != null) {
				Object curriculumIDo = curriculum.get("id");
				if (curriculumIDo != null
						&& !curriculumIDo.toString().isEmpty()) {
					Double d = Double.parseDouble(curriculumIDo.toString());
					curriculum_id = (long) d.intValue();
				}
			}
		} catch (Exception ex) {

		}

		Integer languageyear = saveCastToInteger(record.get("languageyear"));

		Long book_id = saveCastToLong(record.get("book_id"));

		if (!requestData.getSchool_id().equals(
				BookDAO.getSchoolId(book_id, con))) {
			throw new Exception("Book with id " + book_id
					+ " does not belong to school with id "
					+ requestData.getSchool_id());
		}

		BookForm bf = BookFormDAO.insert(book_id, form_id, curriculum_id,
				languageyear, con);

		return new GridResponseSave(GridResponseStatus.success, "", bf.getId());

	}

	@Override
	protected GridResponseUpdateDelete doDelete(GridRequestDelete deleteData,
			Connection con) throws Exception {

		for (Long id : deleteData.getSelected()) {

			if (!deleteData.getSchool_id().equals(
					BookFormDAO.getSchoolId(id, con))) {
				throw new Exception("BookForm-Entry with id " + id
						+ " does not belong to school with id "
						+ deleteData.getSchool_id());
			}

			BookFormDAO.delete(id, con);

		}

		return new GridResponseUpdateDelete(GridResponseStatus.success, "");
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
