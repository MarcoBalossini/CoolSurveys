package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.AuthService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet managing registrations to CoolSurveys
 * Receives only POST requests
 *
 * Respond to user registrations calling authentication services
 */
@WebServlet(name = "DoRegistration", urlPatterns = "/DoRegistration")
@MultipartConfig
public class DoRegistration extends HttpServlet {

    //Form fields
    private final static String USERNAME = "username";
    private final static String MAIL = "mail";
    private final static String PASSWORD = "pwd";
    private final static String CONF_PASSWORD = "confPwd";

    @EJB(name = "it.polimi.db2.coolsurveys.services/AuthService")
    private AuthService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("This servlet only takes POST requests");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usrn = request.getParameter(USERNAME);
        String email = request.getParameter(MAIL);
        String pwd = request.getParameter(PASSWORD);
        String confPwd = request.getParameter(CONF_PASSWORD);

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
