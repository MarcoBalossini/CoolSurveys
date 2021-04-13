package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.JsonObject;
import it.polimi.db2.coolSurveysWEB.utils.FormatUtils;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.IAuthService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet managing registrations to CoolSurveys<br>
 * Receives only POST requests<br>
 * Respond to user registrations calling authentication services
 */
@WebServlet(name = "DoRegistration", urlPatterns = "/DoRegistration")
public class DoRegistration extends HttpServlet {

    //Form fields
    protected final static String USERNAME = "username";
    protected final static String MAIL = "email";
    protected final static String PASSWORD = "password";
    protected final static String CONF_PASSWORD = "passwordConfirm";

    @EJB(name = "it.polimi.db2.coolsurveys.services/AuthService")
    private IAuthService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("This servlet only takes POST requests");
    }

    /**
     * Handle registrations
     * @param request The request containing registration form
     * @param response The response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject;
        try {
            jsonObject = FormatUtils.getJSON(request);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Error: empty request");
            return;
        }

        String usrn = jsonObject.get(USERNAME).getAsString();
        String email = jsonObject.get(MAIL).getAsString();
        String pwd = jsonObject.get(PASSWORD).getAsString();
        String confPwd = jsonObject.get(CONF_PASSWORD).getAsString();

        if (email == null || email.isEmpty() || usrn == null || usrn.isEmpty() ||
                pwd == null || pwd.isEmpty() || confPwd == null || confPwd.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Please, fill all the fields");
            return;
        }

        if (!pwd.equals(confPwd)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Passwords do not match");
            return;
        }

        try {
            Credentials credentials = authService.register(email, usrn, pwd, false);
            if (credentials == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Failed registration");
                return;
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Successful registration");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Failed registration");
            return;
        }

    }
}
