package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.db2.coolSurveysWEB.utils.JsonUtils;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.DAOException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.services.SurveyService;
import it.polimi.db2.coolsurveys.services.exceptions.ServiceException;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

/**
 * This servlet handles admin's requests concerning surveys<br>
 * It supports 3 verbs:
 * <ul>
 *     <li>
 *         <b>GET</b> does 3 different things depending on present parameters:
 *         <ul>
 *             <li>No parameters: Get Map(Date, name) of the past surveys</li>
 *             <li>Only date parameters: Gets responders list for the survey available on the selected date</li>
 *             <li>Date and user parameters: Get user responses to the survey available on the selected date</li>
 *         </ul>
 *     </li>
 *     <li><b>POST</b>: Create a survey for a future date. The parameters are a Map(Question, List(Option)) and the date</li>
 *     <li><b>DELETE</b>: Deletes a past survey given its date as an parameter</li>
 * </ul>
 */
@WebServlet(name = "AdminSurvey", urlPatterns = "/AdminSurvey")
@MultipartConfig
public class AdminSurvey extends HttpServlet {

    protected static class Responders {
        final List<String> submitters;
        final List<String> cancellers;

        public Responders(List<String> submitters, List<String> cancellers) {
            this.submitters = submitters;
            this.cancellers = cancellers;
        }
    }

    protected static class SurveyData {
        final Map<String, String> questionAnswersMap;
        final String age;
        final String gender;
        final String expLvl;

        public SurveyData(Map<String, String> questionAnswersMap, String age, String gender, String expLvl) {
            this.questionAnswersMap = questionAnswersMap;
            this.age = age;
            this.gender = gender;
            this.expLvl = expLvl;
        }
    }

    /**
     * Product's field name in survey's creation
     */
    protected static final String NAME = "name";

    /**
     * Product image's field name in surveys creation
     */
    protected static final String IMAGE = "image";

    /**
     * Date parameter's name for GET and POST requests
     */
    protected static final String DATE = "date";

    /**
     * Map parameter's name for POST requests
     */
    protected static final String MAP = "questionsMap";

    /**
     * User parameter's name for GET requests
     */
    protected static final String USER = "user";

    /**
     * Wrong date format response
     */
    private static final String WRONG_DATE = "Wrong date format";

    /**
     * Injected EJB service for survey related needs
     */
    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveyService")
    private SurveyService surveysService;

    /**
     * If the request has only a date parameter, returns the responders lists (submit + cancel)<br>
     * If the request has a date and a user parameters, returns the response of the user to the survey <br>
     * If no parameter is given, returns the survey list without details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //If d exists get its data
        if(request.getParameter(DATE) != null) {
            String d = request.getParameter(DATE);
            LocalDate date;
            try {
                date = LocalDate.parse(d);
            } catch (DateTimeParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(WRONG_DATE);
                return;
            }

            if(request.getParameter(USER) != null) {
                String usrn = request.getParameter(USER);
                getResponses(request, response, date, usrn);
            }
            else {
                getSurveyResponders(request, response, date);
            }
        }
        //If not get survey list
        else {
            returnSurveys(request, response);
        }
    }

    /**
     * If a form is present, check the survey is not empty, then call SurveysService to register it into the database
     * If not check the given date
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productName = request.getParameter(NAME);
        if(productName != null && !productName.isEmpty()) {
            Part imagePart = request.getPart(IMAGE);
            InputStream imgStream = imagePart.getInputStream();
            byte[] image = new byte[imgStream.available()];
            imgStream.read(image);

            if (image.length==0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Image not present");
                return;
            }

            String json = request.getParameter(MAP);
            LocalDate date;
            try {
                date = LocalDate.parse(request.getParameter(DATE));
            } catch (DateTimeParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(WRONG_DATE);
                return;
            }

            Map<String, List<String>> survey = JsonUtils.convertToMapStringList(new Gson().fromJson(json, JsonElement.class).getAsJsonObject());
            if (survey.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("The submitted survey is empty. Fill it!");
                return;
            }

            try {
                surveysService.createSurvey(productName, survey, date, image);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(date);
                return;
            } catch (AlreadyExistsException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
                return;
            }

        } else {
            try {
                LocalDate date = LocalDate.parse(request.getParameter(DATE));
                if (surveysService.checkDateAvailability(date))
                    response.getWriter().println("true");
                else
                    response.getWriter().println("false");

                response.setStatus(HttpServletResponse.SC_OK);
                return;
            } catch (DateTimeParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(WRONG_DATE);
                return;
            }
        }
    }

    /**
     * Create and return a map date->name for past surveys
     */
    private void returnSurveys(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Questionnaire> surveys = surveysService.getSurveyList();
            Map<LocalDate, String> toSend = new HashMap<>();
            surveys.forEach(survey -> toSend.put(survey.getDate(), survey.getName()));

            String json = new Gson().toJson(toSend);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Some error");
        }
    }

    /**
     * Create and return the lists of users whose:
     * <ul>
     *     <li>Submitted a given survey</li>
     *     <li>Cancelled their responses to the given survey</li>
     * </ul>
     *
     * @param date The survey's date
     */
    private void getSurveyResponders(HttpServletRequest request, HttpServletResponse response, LocalDate date) throws IOException {

        List<String> submitters = new ArrayList<>();

        try {
            surveysService.getSurveyResponders(date, true)
                    .forEach(user -> submitters.add(user.getCredentials().getUsername()));
            List<String> cancellers = new ArrayList<>();
            surveysService.getSurveyResponders(date, false)
                    .forEach(user -> cancellers.add(user.getCredentials().getUsername()));

            Responders toSend = new Responders(submitters, cancellers);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(toSend);
            response.getWriter().println(json);
        } catch (DAOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }

    /**
     * Returns a json string with: Map(Question, Answer), age, gender and expertise level.
     *
     * @param date The survey date
     * @param username The submitter
     */
    private void getResponses(HttpServletRequest request, HttpServletResponse response, LocalDate date, String username) throws IOException {
        try {
            Map<String, String> qaMap = surveysService.getSurveyAnswers(date, username);
            Submission submission = surveysService.getSurveySubmission(date, username);

            String sex = submission.getSex() != null ? capitalize(Submission.Gender.values()[submission.getSex()].name()) : "Unknown";
            String exp_lvl = submission.getExpertiseLevel() != null ?
                    capitalize(Submission.ExpertiseLevel.values()[submission.getExpertiseLevel()].name()) : "Unknown";
            String age = submission.getAge() != null ? submission.getAge().toString() : "Unknown";
            SurveyData toSend = new SurveyData(qaMap, age, sex, exp_lvl);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(toSend);
            response.getWriter().println(json);
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            return;
        }

    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }
}
