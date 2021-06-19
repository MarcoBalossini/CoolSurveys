package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolsurveys.dao.exceptions.DAOException;
import it.polimi.db2.coolsurveys.services.SubmissionService;
import it.polimi.db2.coolsurveys.services.SurveyService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SendImage", urlPatterns = "/static/product")
public class SendImage extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveysService")
    private SurveyService surveysService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            byte[] img = surveysService.getImage();

            response.setContentType("image/jpeg");
            response.setContentLength(img.length);
            response.getWriter().println(img);
        } catch (DAOException e) {
            response.getWriter().println(e.getMessage());
        }
    }
}