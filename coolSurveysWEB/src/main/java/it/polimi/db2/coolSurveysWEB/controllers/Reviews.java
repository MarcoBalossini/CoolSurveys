package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import it.polimi.db2.coolsurveys.services.SurveyService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * Return to the client application the list of product reviews<br>
 * <b>Only GET verb</b>
 */
@WebServlet(name = "Reviews", urlPatterns = "/home/reviews")
public class Reviews extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveysService")
    private SurveyService surveysService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: Fix catch
        try {
            List<String> reviews = surveysService.getReviews();
            response.setStatus(HttpServletResponse.SC_OK);
            String json = new Gson().toJson(reviews);
            response.getWriter().println(json);
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Some error");
            return;
        }
    }
}
