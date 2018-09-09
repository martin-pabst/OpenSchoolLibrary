package de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.LanguageskillDAO;
import de.sp.database.daos.basic.StudentDAO;
import de.sp.database.daos.basic.StudentSchoolTermDAO;
import de.sp.database.model.Student;
import de.sp.database.model.User;
import de.sp.main.services.text.TS;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.library.daos.LibraryDAO;
import de.sp.modules.library.servlets.borrow.borrowerlist.BorrowerRecord;
import de.sp.tools.server.BaseServlet;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LibraryAddStudentServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

		LibraryAddStudentResponse libraryAddStudentResponse;

		try (Connection con = ConnectionPool.beginTransaction()) {

			postData = removeEmptyAttributesFromPostData(postData);


			LibraryAddStundentRequest lasr = gson.fromJson(postData,
					LibraryAddStundentRequest.class);

			lasr.validate(ts);

			LibraryAddStudentRequestRecord record = lasr.record;

			user.checkPermission(LibraryModule.PERMISSION_EDIT_STUDENTS, lasr.school_id);

			Long student_id = lasr.student_id;

			if(student_id == null){

				Student student = StudentDAO.insert(lasr.school_id,record.date_of_birth, record.surname, record.firstname, record.firstname,
						record.before_surname, record.after_surname, record.sex.id.intValue(), "", null, false,
						record.religion.id, con);

				student_id = student.getId();

				StudentSchoolTermDAO.insert(student.getId(), lasr.school_term_id, record.classname.id, record.curriculum.id, con);

			} else {

				StudentDAO.updateBorrower(lasr.student_id,
						record.date_of_birth, record.surname, record.firstname,
						record.before_surname, record.after_surname, record.sex.id,
						record.religion.id, con);

				StudentSchoolTermDAO.updateBorrower(lasr.student_school_term_id, record.curriculum.id, record.classname.id, con);

				LanguageskillDAO.deleteByStudentId(student_id, con);

			}


			for (int i = 1; i <= 3; i++) {

				if (record.getLanguage(i) != null && record.getFromForm(i) != null) {

					Integer from_form = record.getFromForm(i);

					LanguageskillDAO.insert(student_id, record.getLanguage(i).id, from_form, null, con);
				}

			}

			con.commit();

			BorrowerRecord br = LibraryDAO.getBorrower(lasr.school_id, lasr.school_term_id, student_id, con);

			libraryAddStudentResponse = new LibraryAddStudentResponse(br, "success", "");

		} catch (Exception ex) {
			logger.error("Error serving Request", ex);
			libraryAddStudentResponse = new LibraryAddStudentResponse(null, "error", ex.toString());
		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().println(gson.toJson(libraryAddStudentResponse));

	}

	private String removeEmptyAttributesFromPostData(String postData) {

		for(int i = 1; i <= 3; i++) {

			postData = postData.replace(",\"language_" + i + "\":\"\"", "");
			postData = postData.replace(",\"from_form_" + i + "\":\"\"", "");

		}

		return postData;
	}


}
