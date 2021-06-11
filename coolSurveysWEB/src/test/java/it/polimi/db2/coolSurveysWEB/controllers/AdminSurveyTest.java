package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.SubmissionService;
import it.polimi.db2.coolsurveys.services.SurveyService;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static it.polimi.db2.coolSurveysWEB.controllers.AdminSurvey.*;

class AdminSurveyTest {

    @Test
    void testGetSurveyList() {
        SurveyService survService = mock(SurveyService.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try {
            AdminSurvey adminSurvey = new AdminSurvey();
            Field surveysServiceField = adminSurvey.getClass().getDeclaredField("surveysService");
            surveysServiceField.setAccessible(true);
            surveysServiceField.set(adminSurvey, survService);

            List<Questionnaire> surveys = new ArrayList<>();
            surveys.add(new Questionnaire("q1", null, LocalDate.of(1, 1, 1)));
            surveys.add(new Questionnaire("q2", null, LocalDate.of(2, 2, 2)));
            when(survService.getSurveyList()).thenReturn(surveys);

            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(writer);

            adminSurvey.doGet(request, response);
            writer.flush();

            Map<LocalDate, String> sMap = new HashMap<>();
            sMap.put(LocalDate.of(1, 1, 1), "q1");
            sMap.put(LocalDate.of(2, 2, 2), "q2");

            assertTrue(stringWriter.toString().contains(new Gson().toJson(sMap)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetSurveyResponders() {
        SurveyService survService = mock(SurveyService.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter(DATE)).thenReturn("2021-05-16");

        try {
            AdminSurvey adminSurvey = new AdminSurvey();
            Field surveysServiceField = adminSurvey.getClass().getDeclaredField("surveysService");
            surveysServiceField.setAccessible(true);
            surveysServiceField.set(adminSurvey, survService);

            List<User> responders = new ArrayList<>();
            responders.add(new User(new Credentials("a", "b", "c", false)));
            responders.add(new User(new Credentials("d", "e", "f", false)));
            when(survService.getSurveyResponders(any(), eq(true))).thenReturn(responders);
            List<User> cancellers = new ArrayList<>();
            cancellers.add(new User(new Credentials("g", "b", "c", false)));
            cancellers.add(new User(new Credentials("j", "e", "f", false)));
            when(survService.getSurveyResponders(any(), eq(false))).thenReturn(cancellers);

            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(writer);

            adminSurvey.doGet(request, response);
            writer.flush();

            Responders resp = new Responders(List.of("a", "d"), List.of("g", "j"));

            assertEquals(stringWriter.toString().trim(),new Gson().toJson(resp).trim());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetResponses() {
        int age = 20;
        int expLvl = 0;
        int gender = 0;

        SurveyService survService = mock(SurveyService.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter(DATE)).thenReturn("2021-05-16");
        when(request.getParameter(USER)).thenReturn("asd");

        try {
            AdminSurvey adminSurvey = new AdminSurvey();
            Field surveysServiceField = adminSurvey.getClass().getDeclaredField("surveysService");
            surveysServiceField.setAccessible(true);
            surveysServiceField.set(adminSurvey, survService);

            Map<String, String> qaMap = new LinkedHashMap<>();
            qaMap.put("question", "answer");
            when(survService.getSurveyAnswers(any(), anyString())).thenReturn(qaMap);
            Submission sub = new Submission();
            sub.setAge(age);
            sub.setExpertiseLevel(expLvl);
            sub.setSex(gender);
            when(survService.getSurveySubmission(any(), anyString())).thenReturn(sub);

            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(writer);

            adminSurvey.doGet(request, response);
            writer.flush();

            SurveyData resp = new SurveyData(qaMap, age, "Male", "Low");

            assertEquals(stringWriter.toString().trim(),new Gson().toJson(resp).trim());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}