package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolSurveysWEB.controllers.CheckLogin;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.AuthService;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static it.polimi.db2.coolSurveysWEB.controllers.CheckLogin.*;


class CheckLoginTest {


    @Test
    public void testFormLogin() throws Exception {

        //Test null String value
        testNoDB(null, "pwd", "Missing credential value");

        //Test empty String value
        testNoDB("", "pwd", "Missing credential value");

        //Test wrong credentials
        testDB("usrn", "pwd", "Invalid credentials", null, false);

        //Test correct credentials
        Credentials credentials = new Credentials(1, "a", "b", "c", false);
        testDB("usrn", "pwd", credentials.getUsername(), credentials, false);

        //Test catch
        testDB("usrn", "pwd", "Operation not allowed", credentials, true);
    }

    private void testNoDB(String usrn, String pwd, String msg) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter(USERNAME)).thenReturn(usrn);
        when(request.getParameter(PASSWORD)).thenReturn(pwd);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new CheckLogin().doPost(request, response);

        verify(request, atLeast(1)).getParameter(PASSWORD);
        verify(request, atLeast(1)).getParameter(USERNAME);
        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    private void testDB(String usrn, String pwd, String msg, Credentials credentials, boolean throwException) throws Exception {
        //Create mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        AuthService authenticationService = mock(AuthService.class);

        //Use Reflection on check login class to mock injected service
        CheckLogin checkLogin = new CheckLogin();
        Field authServiceField = checkLogin.getClass().getDeclaredField("authService");
        authServiceField.setAccessible(true);
        authServiceField.set(checkLogin, authenticationService);

        //Mocks' responses
        when(request.getParameter(USERNAME)).thenReturn(usrn);
        when(request.getParameter(PASSWORD)).thenReturn(pwd);
        when(request.getSession()).thenReturn(session);

        if (!throwException)
            when(authenticationService.checkCredentials(anyString(), anyString())).thenReturn(credentials);
        else
            when(authenticationService.checkCredentials(anyString(), anyString())).thenThrow(new Exception(""));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        checkLogin.doPost(request, response);

        verify(request, atLeast(1)).getParameter(PASSWORD);
        verify(request, atLeast(1)).getParameter(USERNAME);
        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

}