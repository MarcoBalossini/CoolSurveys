package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolSurveysWEB.auth.AuthManager;
import it.polimi.db2.coolSurveysWEB.auth.exceptions.TokenException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.AuthService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * Servlet managing login to CoolSurveys.<br>
 * Two login methods are offered:
 * <ul>
 *     <li>Token login (GET requests)</li>
 *     <li>Form login (POST requests)</li>
 * </ul>
 */
@WebServlet(name = "CheckLogin", urlPatterns = "/CheckLogin")
@MultipartConfig
public class CheckLogin extends HttpServlet {

    //Form fields
    /**
     * Password form field name
     */
    protected final static String PASSWORD = "pwd";

    /**
     * Username form field name
     */
    protected final static String USERNAME = "username";

    //Cookies
    /**
     * Token name
     */
    protected final static String AUTH_TOKEN = "auth-token";

    /**
     * Token validity
     */
    protected static final long EXPIRATION_TIME = 1000*60*60*24;

    @EJB(name = "it.polimi.db2.coolsurveys.services/AuthService")
    private AuthService authService;

    /**
     * Handle token login
     * @param request The request containing auth token
     * @param response The response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("No cookies found");
            return;
        }

        Cookie authCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(AUTH_TOKEN))
                .findAny()
                .orElse(null);

        if (authCookie == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Auth cookie not found");
            return;
        }

        Credentials credentials;
        try {
            int userId = AuthManager.getInstance().checkToken(authCookie.getValue());
            credentials = authService.tokenLogin(userId);
        } catch (TokenException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid token. Login again");
            return;
        }

        request.getSession().setAttribute("user", credentials);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(credentials.getUsername());
    }

    /**
     * Handle form login
     * @param request The request containing login form data
     * @param response The response
     * @throws ServletException
     * @throws IOException
     */
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
            String token = authManager.generateToken(credentials.getUser_id(), EXPIRATION_TIME);
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
