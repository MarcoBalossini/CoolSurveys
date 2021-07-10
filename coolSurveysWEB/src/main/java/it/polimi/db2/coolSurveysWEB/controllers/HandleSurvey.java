package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.db2.coolSurveysWEB.utils.JsonUtils;
import it.polimi.db2.coolSurveysWEB.utils.ResponseQuestionnaire;
import it.polimi.db2.coolsurveys.services.SurveyService;
import it.polimi.db2.coolsurveys.services.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.DAOException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.services.SubmissionService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This servlet handles surveys requests and submissions
 */
@WebServlet(name = "HandleSurvey", urlPatterns = "/HandleSurvey")
@MultipartConfig
public class HandleSurvey extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/SubmissionService")
    private SubmissionService submissionService;

    @EJB(name = "it.polimi.db2.coolsurveys.services/SurveysService")
    private SurveyService surveysService;

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
        Questionnaire questionnaire;
        try {
            questionnaire = submissionService.retrieveDailySurvey((Credentials) request.getSession().getAttribute("user"));
        } catch (DAOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            return;
        }


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

        Credentials credentials = (Credentials) request.getSession().getAttribute("user");

        JsonObject json = JsonUtils.getJsonFromRequest(request);

        List<JsonObject> permanentQuestions = JsonUtils.getInstance().getPermanentQuestions();
        List<String> sec2Answers = new ArrayList<>();

        for (JsonObject q : permanentQuestions) {
            List<String> stringOptions = new ArrayList<>();
            String answer;
            try {
                String question = q.get("question").getAsString();
                answer = json.get(question).getAsString();
                q.get("options").getAsJsonArray().forEach(option -> stringOptions.add(option.getAsString()));

                if (!stringOptions.isEmpty() && !stringOptions.contains(answer) && !answer.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("An answer to section 2 question is not correct");
                    return;
                }

                json.remove(question);
                sec2Answers.add(answer);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("An error occurred in section 2 questions");
                return;
            }
        }

        try {
            Integer age = null;
            if (!sec2Answers.get(0).isEmpty())
                age = Integer.parseInt(sec2Answers.get(0));

            //If some question has no answer status = 400
            Map<String, String> qaMap = JsonUtils.convertToMap(json);
            for (Map.Entry<String, String> entry : qaMap.entrySet()) {
                if(entry.getValue() == null || entry.getValue().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Answer all the questions from part 1");
                    return;
                }
            }

            //If the number of given and expected answers are different status = 400
            if(qaMap.size() != surveysService.getQuestionCount()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Answer all the questions from part 1");
                return;
            }

            submissionService.registerSubmission(qaMap, credentials, age, sec2Answers.get(1), sec2Answers.get(2));
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Age must be a number");
        } catch (BadWordFoundException e) {
            submissionService.blockUser(credentials);

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Your answers contained swear words. Your account will be blocked");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }

    /**
     * Create response objects by removing internal information from EJB entities
     * @param q The Questionnaire entity
     * @return The ResponseQuestionnaire object
     */
    private ResponseQuestionnaire createResponseObject(Questionnaire q) {

        ResponseQuestionnaire questionnaire = new ResponseQuestionnaire(q.getName(), q.getDate());

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
                question -> new ResponseQuestionnaire.ResponseQuestion(question.getId(), question.getQuestion(), 1)
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

        //Add permanent questions
        addPermanentQuestions(questionnaire);

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

    protected void addPermanentQuestions(ResponseQuestionnaire questionnaire) {

        List<JsonObject> jsonQuestions = JsonUtils.getInstance().getPermanentQuestions();

        List<ResponseQuestionnaire.ResponseQuestion> questions = new ArrayList<>();
        int i = questionnaire.getNumberOfQuestions();
        for (JsonObject q : jsonQuestions) {
            i++;
            ResponseQuestionnaire.ResponseQuestion question =
                    new ResponseQuestionnaire.ResponseQuestion(i, q.get("question").getAsString(), 2, q.get("type").getAsString());
            List<String> stringOptions = new ArrayList<>();
            q.get("options").getAsJsonArray().forEach(option -> stringOptions.add(option.getAsString()));

            int j = 1;
            List<ResponseQuestionnaire.ResponseOption> options = new ArrayList<>();
            for(String optionText : stringOptions) {
                options.add(new ResponseQuestionnaire.ResponseOption(j, optionText));
                j++;
            }
            question.setOptions(options);

            questions.add(question);
        }

        questionnaire.addQuestions(questions);
    }

}