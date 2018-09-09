package de.sp.main.mainframe.definitionsservlet;

import com.google.gson.Gson;
import de.sp.database.model.School;
import de.sp.database.model.SchoolTerm;
import de.sp.database.model.User;
import de.sp.database.stores.SchoolTermStore;
import de.sp.main.services.text.TS;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.server.ErrorResponse;
import org.slf4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DefinitionsServlet extends BaseServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
    }

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        String json = "{}";

        Gson gson = new Gson();
        DefinitionRequest dr = gson.fromJson(postData, DefinitionRequest.class);

        try {
            SchoolTerm schoolTerm = null;
            School school = null;

            if (dr.getSchool_term_id() != null) {

                schoolTerm = SchoolTermStore.getInstance().getSchoolTerm(
                        dr.getSchool_term_id());

                if (schoolTerm == null) {
                    throw new Exception("Missing school_term-id");
                }

                school = schoolTerm.getSchool();

                if (!user.hasAnyPermissionForSchool(school)) {
                    throw new Exception("User has no rights for given school.");
                }
            }

            Definitions definitions = new Definitions(user, school,
                    schoolTerm);

            json = gson.toJson(definitions);

        } catch (Exception ex) {
            logger.error("Error on retrieving valuelists: ", ex);
            json = gson.toJson(new ErrorResponse(ex.toString()));
        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(json);

    }

}
