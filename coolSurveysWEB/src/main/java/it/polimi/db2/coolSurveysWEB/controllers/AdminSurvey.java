package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.db2.coolSurveysWEB.utils.JsonUtils;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.services.SubmissionService;
import it.polimi.db2.coolsurveys.services.SurveyService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

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
        final int age;
        final String gender;
        final String expLvl;

        public SurveyData(Map<String, String> questionAnswersMap, int age, String gender, String expLvl) {
            this.questionAnswersMap = questionAnswersMap;
            this.age = age;
            this.gender = gender;
            this.expLvl = expLvl;
        }
    }

    /**
     * Date parameter's name for GET requests
     */
    protected static final String DATE = "date";

    /**
     * User parameter's name for GET requests
     */
    protected static final String USER = "user";

    /**
     * Injected EJB service for survey related needs
     */
    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveysService")
    private SurveyService surveysService;

    /**
     * If the request has a date parameter, returns the responders lists (submit + cancel)<br>
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
                response.getWriter().println("Wrong date format");
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
     * Check the survey is not empty, then call SurveysService to register it into the database
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var d = request.getParameter(DATE);

        // If a date parameter is present we check its availability
        // If available status = 200 (OK)
        // If unavailable status = 403 (FORBIDDEN)
        if (d != null) {
            try {
                LocalDate date = LocalDate.parse(d);
                if (surveysService.checkDateAvailability(date))
                    response.getWriter().println("true");
                else
                    response.getWriter().println("false");

                response.setStatus(HttpServletResponse.SC_OK);

            } catch (DateTimeParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("The parameter is not formatted as a date");
                return;
            }
            //TODO: Add catches
        } else {
            JsonObject json = JsonUtils.getJsonFromRequest(request);

            Map<String, List<String>> survey = JsonUtils.convertToMapStringList(json);

            if (survey.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("The submitted survey is empty. Fill it!");
                return;
            }

            //TODO: Fix catch with correct exception/s
            try {
                LocalDate date = surveysService.createSurvey(survey);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(date);
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Some server error occurred");
                return;
            }
        }
    }

    /**
     * Deletes surveys given a Dates array
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject json = JsonUtils.getJsonFromRequest(request);
        List<LocalDate> dates = new ArrayList<>();

        try {
            new Gson().fromJson(json, JsonArray.class).forEach(d -> dates.add(LocalDate.parse(d.getAsString())));
        } catch (DateTimeParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid dates");
            return;
        }

        //TODO: Fix catch
        try {
            surveysService.deleteSurveys(dates);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Some error");
        }
    }

    /**
     * Create and return a map date->name for past surveys
     */
    private void returnSurveys(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO: Fix catch
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
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
    }

    /**
     * Returns a json string with: Map(Question, Answer), age, gender and expertise level.
     *
     * @param date The survey date
     * @param username The submitter
     */
    private void getResponses(HttpServletRequest request, HttpServletResponse response, LocalDate date, String username) throws IOException {
        //TODO: Fix catch
        try {
            Map<String, String> qaMap = surveysService.getSurveyAnswers(date, username);
            Submission submission = surveysService.getSurveySubmission(date, username);

            SurveyData toSend = new SurveyData(qaMap, submission.getAge(), capitalize(Submission.Gender.values()[submission.getSex()].name()),
                    capitalize(Submission.ExpertiseLevel.values()[submission.getExpertiseLevel()].name()));

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(toSend);
            response.getWriter().println(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Some error");
            return;
        }

    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }
}
