package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolSurveysWEB.auth.AuthManager;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.AuthService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Servlet managing registrations to CoolSurveys
 * Receives only POST requests
 *
 * Respond to user registrations calling authentication services
 */
@WebServlet(name = "CheckLogin", urlPatterns = "/CheckLogin")
@MultipartConfig
public class CheckLogin extends HttpServlet {

    //Form fields
    protected final static String PASSWORD = "pwd";
    protected final static String USERNAME = "username";

    //Cookies
    protected final static String AUTH_TOKEN = "auth-token";

    @EJB(name = "it.polimi.db2.coolsurveys.services/AuthService")
    private AuthService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie authCookie = Arrays.asList(request.getCookies()).stream()
                .filter(cookie -> cookie.getName().equals(AUTH_TOKEN))
                .findAny()
                .orElse(null);

        if (authCookie == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Auth cookie not found");
            return;
        }

        if (AuthManager.getInstance().checkToken(authCookie.getValue())) {
            //Token login
            return;
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("Invalid token. Login again");
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pwd = request.getParameter(PASSWORD);
        String usrn = request.getParameter(USERNAME);

        if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing credential value");
            return;
        }

        try {
            Credentials credentials = authService.checkCredentials(usrn, pwd);

            if (credentials == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid credentials");
                return;
            }

            AuthManager authManager = AuthManager.getInstance();
            String token = authManager.generateToken(usrn);
            if (token == null || token.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Cannot generate token");
                return;
            }

            Cookie authCookie  = new Cookie(AUTH_TOKEN, token);
            authCookie.setMaxAge(24*60*60);
            response.addCookie(authCookie);

            request.getSession().setAttribute("user", credentials);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(credentials.getUsername());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Operation not allowed");
            return;
        }

    }
}
