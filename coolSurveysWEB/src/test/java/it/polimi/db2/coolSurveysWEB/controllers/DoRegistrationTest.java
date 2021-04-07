package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.AuthService;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static it.polimi.db2.coolSurveysWEB.controllers.CheckLogin.PASSWORD;
import static it.polimi.db2.coolSurveysWEB.controllers.CheckLogin.USERNAME;
import static it.polimi.db2.coolSurveysWEB.controllers.DoRegistration.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;

class DoRegistrationTest {

    @Test
    public void testRegistration() throws Exception {
        // Test null values
        testNoDB("usrn", null, null, null, "Please, fill all the fields");

        // Test empty values
        testNoDB("", "mail", "pwd", "pwd", "Please, fill all the fields");

        // Test different passwords
        testNoDB("usrn", "mail@mail", "pwd", "differentPwd", "Passwords do not match");

        // Test non existent credentials
        testMockDB("usrn", "mail@mail", "pwd", "pwd", "Failed registration", null, false);

        // Test existing credentials
        Credentials credentials = new Credentials(1, "a", "b", "c", false);
        testMockDB("usrn", "mail@mail", "pwd", "pwd", "Successful registration", credentials, false);

        // Test thrown exception
        testMockDB("usrn", "mail@mail", "pwd", "pwd", "Failed registration", null, true);
    }

    private void testNoDB(String usrn, String mail, String pwd, String pwdConf, String msg) throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        mockParams(mail, usrn, pwd, pwdConf, request);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new DoRegistration().doPost(request, response);

        verify(request, atLeast(1)).getParameter(PASSWORD);
        verify(request, atLeast(1)).getParameter(USERNAME);
        verify(request, atLeast(1)).getParameter(MAIL);
        verify(request, atLeast(1)).getParameter(CONF_PASSWORD);

        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    private void testMockDB(String usrn, String mail, String pwd, String pwdConf,
                            String msg, Credentials credentials, boolean throwException) throws Exception {
        //Create mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        AuthService authenticationService = mock(AuthService.class);

        //Use Reflection on check login class to mock injected service
        DoRegistration doRegistration = new DoRegistration();
        Field authServiceField = doRegistration.getClass().getDeclaredField("authService");
        authServiceField.setAccessible(true);
        authServiceField.set(doRegistration, authenticationService);

        //Mocks' responses
        mockParams(mail, usrn, pwd, pwdConf, request);
        when(request.getSession()).thenReturn(session);

        if (!throwException)
            when(authenticationService.register(anyString(), anyString(), anyString(), anyBoolean())).thenReturn(credentials);
        else
            when(authenticationService.register(anyString(), anyString(), anyString(), anyBoolean())).thenThrow(new Exception(""));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        doRegistration.doPost(request, response);

        verify(request, atLeast(1)).getParameter(PASSWORD);
        verify(request, atLeast(1)).getParameter(USERNAME);
        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new DoRegistration().doGet(request, response);
        writer.flush();

        verify(response, atLeast(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertTrue(stringWriter.toString().contains("This servlet only takes POST requests"));
    }

    private void mockParams(String mail, String usrn, String pwd, String pwdConf, HttpServletRequest request) {
        when(request.getParameter(MAIL)).thenReturn(mail);
        when(request.getParameter(USERNAME)).thenReturn(usrn);
        when(request.getParameter(PASSWORD)).thenReturn(pwd);
        when(request.getParameter(CONF_PASSWORD)).thenReturn(pwdConf);
    }

}