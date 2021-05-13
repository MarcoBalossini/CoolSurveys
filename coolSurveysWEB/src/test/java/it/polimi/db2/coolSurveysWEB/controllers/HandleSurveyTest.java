package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.db2.coolSurveysWEB.utils.JsonUtils;
import it.polimi.db2.coolSurveysWEB.utils.ResponseQuestionnaire;
import it.polimi.db2.coolsurveys.dao.exceptions.DAOException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.Option;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.services.ISurveysService;
import it.polimi.db2.coolsurveys.services.SurveysService;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HandleSurveyTest {

    @Test
    public void testSurveyRetrieval() throws IOException, IllegalAccessException, ServletException, NoSuchFieldException {

        //Test questionnaire not found
        testGetMockDB(null, "Survey not found", null);

        //Test questionnaire found
        String questionnaireName = "testQuestionnaire";
        byte[] photo = null;
        String questionText = "What should I ask you?";
        String question2Text = "What should I ask you?2";
        String optionText = "Option1";
        String option2Text = "Option2";

        Questionnaire questionnaire = new Questionnaire(questionnaireName, photo, null);
        Question question = new Question(1, questionText);
        Question question2 = new Question(2, question2Text);
        Option option = new Option(3, optionText);
        Option option2 = new Option(4, option2Text);
        question.setOptions(List.of(option, option2));
        questionnaire.setQuestions(List.of(question, question2));

        ResponseQuestionnaire responseQuestionnaire = new ResponseQuestionnaire(questionnaireName, photo, null);
        ResponseQuestionnaire.ResponseQuestion responseQuestion = new ResponseQuestionnaire.ResponseQuestion(1, questionText, 1);
        ResponseQuestionnaire.ResponseQuestion responseQuestion2 = new ResponseQuestionnaire.ResponseQuestion(2, question2Text, 1);
        ResponseQuestionnaire.ResponseOption responseOption = new ResponseQuestionnaire.ResponseOption(1, optionText);
        ResponseQuestionnaire.ResponseOption responseOption2 = new ResponseQuestionnaire.ResponseOption(2, option2Text);
        responseQuestion.setOptions(List.of(responseOption, responseOption2));
        responseQuestionnaire.setQuestions(List.of(responseQuestion, responseQuestion2));

        testGetMockDB(questionnaire, null, responseQuestionnaire);
    }

    private void testGetMockDB(Questionnaire questionnaire, String msg, ResponseQuestionnaire responseQuestionnaire)
            throws NoSuchFieldException, IllegalAccessException, IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ISurveysService surveysService = mock(SurveysService.class);

        HttpSession session = mock(HttpSession.class);

        HandleSurvey handleSurvey = new HandleSurvey();
        Field surveyServiceField = handleSurvey.getClass().getDeclaredField("surveysService");
        surveyServiceField.setAccessible(true);
        surveyServiceField.set(handleSurvey, surveysService);

        when(request.getSession()).thenReturn(session);

        Credentials credentials = new Credentials("user1", "user", "user@user.it", false);
        credentials.setUserId(1);

        when(session.getAttribute("user")).thenReturn(credentials);



        try {
            when(surveysService.retrieveDailySurvey()).thenReturn(questionnaire);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        handleSurvey.doGet(request, response);

        if (responseQuestionnaire != null) {
            handleSurvey.addPermanentQuestions(responseQuestionnaire);
//            String responseQuestionnaireJson = new Gson().toJson(responseQuestionnaire);
//            assertEquals(responseQuestionnaireJson.trim(), stringWriter.toString().trim());
            ResponseQuestionnaire createdResponse = new Gson().fromJson(stringWriter.toString(), ResponseQuestionnaire.class);
            assertEquals(createdResponse, responseQuestionnaire);
        } else {
            assertTrue(stringWriter.toString().contains(msg));
        }
    }

    @Test
    public void testSurveySubmission() throws IOException, ServletException, NoSuchFieldException, IllegalAccessException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ISurveysService surveysService = mock(SurveysService.class);
        HttpSession session = mock(HttpSession.class);

        Map<String, String> questionsAnswersMap = new HashMap<>();
        questionsAnswersMap.put("key1", "val1");
        questionsAnswersMap.put("key2", "val2");


        HandleSurvey handleSurvey = new HandleSurvey();
        Field surveyServiceField = handleSurvey.getClass().getDeclaredField("surveysService");
        surveyServiceField.setAccessible(true);
        surveyServiceField.set(handleSurvey, surveysService);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Credentials());

        List<JsonObject> pQuestions = JsonUtils.getInstance().getPermanentQuestions();
        for(JsonObject j : pQuestions) {
            List<String> stringOptions = new ArrayList<>();
            j.get("options").getAsJsonArray().forEach((option) -> stringOptions.add(option.getAsString()));

            if (!stringOptions.isEmpty())
                questionsAnswersMap.put(j.get("question").getAsString(), stringOptions.get(0));
            else
                questionsAnswersMap.put(j.get("question").getAsString(), "42");
        }

        mockParams(questionsAnswersMap, request);

        handleSurvey.doPost(request, response);

        verify(response, atLeast(1)).setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    public static void mockParams(Map<String, String> questionsAnswersMap, HttpServletRequest request) throws IOException {

        String json = new Gson().toJson(questionsAnswersMap);

        Reader inputString = new StringReader(json);
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
    }

}