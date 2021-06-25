package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolsurveys.dao.exceptions.DAOException;
import it.polimi.db2.coolsurveys.services.SubmissionService;
import it.polimi.db2.coolsurveys.services.SurveyService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "SendImage", urlPatterns = "/static/product")
public class SendImage extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveysService")
    private SurveyService surveysService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            byte[] img = surveysService.getImage();

            String b64 = new String(Base64.getEncoder().encode(img));

//            response.setContentType("image/jpeg");
//            response.setContentLength(img.length);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(b64);
        } catch (DAOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}