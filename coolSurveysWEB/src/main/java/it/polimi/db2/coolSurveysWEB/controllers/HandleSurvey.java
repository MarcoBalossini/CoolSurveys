package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import it.polimi.db2.coolSurveysWEB.utils.ResponseQuestionnaire;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.services.SurveysService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This servlet handles surveys requests and submissions
 */
@WebServlet(name = "HandleSurvey", urlPatterns = {"/HandleSurvey"})
public class HandleSurvey extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveyService")
    private SurveysService surveysService;

    /**
     * Respond to daily surveys requests
     *
     * Codes:
     * <ul>
     *     <li><b>200</b>: Questionnaire found and sent</li>
     *     <li><b>500</b>: Survey not found</li>
     * </ul>
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Questionnaire questionnaire = surveysService.retrieveDailySurvey();

        if (questionnaire == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Survey not found");
            return;
        }

        ResponseQuestionnaire questionnaireToSend = createResponseObject(questionnaire);

        String json = new Gson().toJson(questionnaireToSend);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(json);
    }

    /**
     * Respond to survey's submissions<br>
     *
     * Codes:
     * <ul>
     *     <li><b>200</b>: Successful submission</li>
     *     <li><b>400</b>: Request with wrong parameters</li>
     * </ul>
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("This servlet only takes GET requests");
    }

    /**
     * Create response objects by removing internal information from EJB entities
     * @param q The Questionnaire entity
     * @return The ResponseQuestionnaire object
     */
    private ResponseQuestionnaire createResponseObject(Questionnaire q) {

        ResponseQuestionnaire questionnaire = new ResponseQuestionnaire(q.getName(), q.getPhoto(), q.getDate());

        //Create options
        Map<Question, List<ResponseQuestionnaire.ResponseOption>> optPerQuestion = new HashMap<>();
        for (Question question : q.getQuestions()) {
            List<ResponseQuestionnaire.ResponseOption> options = question.getOptions().stream().map(
                    option -> new ResponseQuestionnaire.ResponseOption(option.getId(), option.getText())
            ).collect(Collectors.toList());

            //Renumber options from 1 on
            int i = 1;
            options.sort(Comparator.comparing(ResponseQuestionnaire.ResponseOption::getNumber));
            for (ResponseQuestionnaire.ResponseOption option : options) {
                option.setNumber(i);
                i++;
            }
            optPerQuestion.put(question, options);
        }

        //Create questions
        List<ResponseQuestionnaire.ResponseQuestion> questions = q.getQuestions().stream().map(
                question -> new ResponseQuestionnaire.ResponseQuestion(question.getId(), question.getQuestion())
        ).collect(Collectors.toList());

        //Renumber questions from 1 on
        int i = 1;
        questions.sort(Comparator.comparing(ResponseQuestionnaire.ResponseQuestion::getNumber));
        for (ResponseQuestionnaire.ResponseQuestion question : questions) {
            question.setNumber(i);
            i++;
        }

        //Add options to questions
        for (ResponseQuestionnaire.ResponseQuestion question : questions) {
            question.setOptions(optPerQuestion.get(responseToRealQuestion(question, q.getQuestions())));
        }

        //Add questions to questionnaire
        questionnaire.setQuestions(questions);

        return questionnaire;
    }

    private Question responseToRealQuestion(ResponseQuestionnaire.ResponseQuestion responseQuestion, List<Question> questions) {
        //There aren't questions with the same text -> List with only one element
        //As assumption data from database are correct
        return questions.stream()
                        .filter(q -> q.getQuestion().equals(responseQuestion.getQuestion()))
                        .collect(Collectors.toList())
                        .get(0);
    }

}