package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import it.polimi.db2.coolSurveysWEB.utils.FormatUtils;
import it.polimi.db2.coolSurveysWEB.utils.ResponseQuestionnaire;
import it.polimi.db2.coolsurveys.entities.Option;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.services.ISurveysService;
import it.polimi.db2.coolsurveys.services.SurveysService;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        HandleSurvey handleSurvey = new HandleSurvey();
        Field surveyServiceField = handleSurvey.getClass().getDeclaredField("surveysService");
        surveyServiceField.setAccessible(true);
        surveyServiceField.set(handleSurvey, surveysService);

        when(surveysService.retrieveDailySurvey()).thenReturn(questionnaire);

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

}