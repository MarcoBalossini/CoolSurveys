package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.JsonObject;
import it.polimi.db2.coolSurveysWEB.utils.JsonUtils;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.IAuthService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
@MultipartConfig
@WebServlet(name = "DoRegistration", urlPatterns = "/DoRegistration")
public class DoRegistration extends HttpServlet {

    //Form fields
    public final static String USERNAME = "username";
    public final static String MAIL = "email";
    public final static String PASSWORD = "password";
    public final static String CONF_PASSWORD = "passwordConfirm";

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

        String usrn;
        String email;
        String pwd;
        String confPwd;

        JsonObject jsonObject;
        try {
            jsonObject = JsonUtils.getJsonFromRequest(request);
            usrn = jsonObject.get(USERNAME).getAsString();
            email = jsonObject.get(MAIL).getAsString();
            pwd = jsonObject.get(PASSWORD).getAsString();
            confPwd = jsonObject.get(CONF_PASSWORD).getAsString();

            if (email.isEmpty() || usrn.isEmpty() || pwd.isEmpty() || confPwd.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Please, fill all the fields");
                return;
            }
        } catch (Exception e) {
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
            response.setContentType("text/plain");
            response.getWriter().println("Successful registration");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Failed registration");
        }

    }
}
