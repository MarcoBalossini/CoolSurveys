package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolsurveys.services.SubmissionService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SendImage", urlPatterns = "/static/product")
public class SendImage extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveysService")
    private SubmissionService surveysService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] img = surveysService.getImage();

        response.setContentType("image/jpeg");
        response.setContentLength(img.length);
        response.getWriter().println(img);
    }
}