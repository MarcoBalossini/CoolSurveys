package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.db2.coolSurveysWEB.utils.JsonUtils;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.services.SurveyService;
import it.polimi.db2.coolsurveys.services.exceptions.ServiceException;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminDelete", urlPatterns = "/admin/delete")
public class AdminDelete extends HttpServlet {

    /**
     * Injected EJB service for survey related needs
     */
    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveyService")
    private SurveyService surveysService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = JsonUtils.getJson(request);
        List<LocalDate> dates = new ArrayList<>();

        try {
            new Gson().fromJson(json, JsonArray.class).forEach(d -> dates.add(LocalDate.parse(d.getAsString())));
        } catch (DateTimeParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid dates");
            return;
        }

        try {
            surveysService.deleteSurveys(dates);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ServiceException | NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}
