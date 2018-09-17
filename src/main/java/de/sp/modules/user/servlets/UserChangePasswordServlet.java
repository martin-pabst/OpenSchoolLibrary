package de.sp.modules.user.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.model.User;
import de.sp.main.services.text.TS;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.string.PasswordSecurity;
import de.sp.tools.string.SaltAndHash;
import org.slf4j.Logger;
import org.sql2o.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserChangePasswordServlet extends BaseServlet {

    @Override
    protected void doPostExtended(HttpServletRequest request,
                                  HttpServletResponse response, Logger logger, HttpSession session,
                                  User user, TS ts, String postData) throws ServletException,
            IOException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

        String responseString = "";

        try (Connection con = ConnectionPool.beginTransaction()) {

            try {

                ChangePasswordRequest cpr = gson.fromJson(postData, ChangePasswordRequest.class);

                cpr.validate(ts);

                SaltAndHash saltAndHash = new SaltAndHash(user.getSalt(),
                        user.getHash());

                boolean credentialsOK = PasswordSecurity.check(cpr.getOldPassword(),
                        saltAndHash);

                if(!credentialsOK){
                    throw new ChangePasswordException("warning_sign Das alte Passwor ist falsch.");
                }

                if(!cpr.getNewPassword1().equals(cpr.getNewPassword2())){
                    throw new ChangePasswordException("warning_sing Die angegebenen neuen Passwörter stimmen nicht überein.");
                }

                user.setPassword(cpr.getNewPassword1());

                UserDAO.update(user, con);

                responseString = gson.toJson(new ChangePasswordResponse("success", "Das Passwort wurde erfolgreich geändert."));

                con.commit(true);

            } catch (Exception ex) {
                if(!(ex instanceof ChangePasswordException)) {
                    logger.error("Error serving Request", ex);
                }
                con.rollback();
                responseString = gson.toJson(new ChangePasswordResponse("error", ex.toString()));
            }
        }

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(responseString);

    }




}
