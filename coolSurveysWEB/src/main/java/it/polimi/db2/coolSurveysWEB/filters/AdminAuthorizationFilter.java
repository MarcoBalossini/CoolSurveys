package it.polimi.db2.coolSurveysWEB.filters;

import it.polimi.db2.coolsurveys.entities.Credentials;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AdminAuthorizationFilter")
public class AdminAuthorizationFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String homePath = req.getServletContext().getContextPath() + "/userHome.html";
        String indexPath = req.getServletContext().getContextPath() + "/index.html";

        try {
            Credentials user = (Credentials) req.getSession().getAttribute("user");
            if (!user.isAdmin()) {
                res.sendRedirect(homePath);
                return;
            }
        } catch (Exception e) {
            res.sendRedirect(indexPath);
            return;
        }

        chain.doFilter(request, response);
    }
}
