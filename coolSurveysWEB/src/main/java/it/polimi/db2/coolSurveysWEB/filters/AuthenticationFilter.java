package it.polimi.db2.coolSurveysWEB.filters;

import it.polimi.db2.coolSurveysWEB.auth.AuthManager;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.IAuthService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Checks that every request comes from a user with a valid session.<br>
 * If so, the request proceeds. Otherwise the user is sent to login.<br>
 * If the authCookie is present and has a valid token, the user is logged in. Otherwise the user is redirected to index.html
 */
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    /**
     * Token name
     */
    protected static final String AUTH_TOKEN = "auth-token";

    @EJB(name = "it.polimi.db2.coolsurveys.services/AuthService")
    private IAuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String loginPath = req.getServletContext().getContextPath() + "/index.html";
        String userPath = req.getServletContext().getContextPath() + "/userHome.html";
        String adminPath = req.getServletContext().getContextPath() + "/admin.html";

        if (req.getRequestURI().contains("index.html") || req.getRequestURI().contains("CheckLogin")  ||
                req.getRequestURI().contains("DoRegistration") || req.getRequestURI().contains("css") ||
                req.getRequestURI().contains("js")) {
            chain.doFilter(request, response);
            return;
        }


        if(req.getSession().getAttribute("user") == null){
            Cookie[] cookies = req.getCookies();
            if (cookies == null) {
                res.sendRedirect(loginPath);
                return;
            }

            Cookie authCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(AUTH_TOKEN))
                    .findAny()
                    .orElse(null);

            if (authCookie == null) {
                res.sendRedirect(loginPath);
                return;
            }

            Credentials credentials;
            try {
                int userId = AuthManager.getInstance().checkToken(authCookie.getValue());
                credentials = authService.tokenLogin(userId);
            } catch (Exception e) {
                res.sendRedirect(loginPath);
                System.out.println(e.getMessage());
                return;
            }

            req.getSession().setAttribute("user", credentials);

            if (credentials.isAdmin()) {
                res.setStatus(HttpServletResponse.SC_OK);
                res.sendRedirect(adminPath);
            } else {
                res.setStatus(HttpServletResponse.SC_OK);
                res.sendRedirect(userPath);
            }

            return;
        }

        chain.doFilter(request, response);
    }
}
