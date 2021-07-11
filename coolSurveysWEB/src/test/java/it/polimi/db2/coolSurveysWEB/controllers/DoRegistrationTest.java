package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.IAuthService;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Field;

import static it.polimi.db2.coolSurveysWEB.controllers.DoRegistration.*;
import static it.polimi.db2.coolSurveysWEB.controllers.DoRegistration.CONF_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;

class DoRegistrationTest {

    @Test
    void testRegistration() throws Exception {
        // Test null values
        testNoDB("usrn", null, null, null, "Please, fill all the fields");

        // Test empty values
        testNoDB("", "mail", "pwd", "pwd", "Please, fill all the fields");

        // Test different passwords
        testNoDB("usrn", "mail@mail", "pwd", "differentPwd", "Passwords do not match");

        // Test non existent credentials
        testMockDB("usrn", "mail@mail", "pwd", "pwd", "Failed registration", null, false);

        // Test existing credentials
        Credentials credentials = new Credentials("a", "b", "c", false);
        credentials.setUserId(1);
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

        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    private void testMockDB(String usrn, String mail, String pwd, String pwdConf,
                            String msg, Credentials credentials, boolean throwException) throws Exception {
        //Create mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        IAuthService authenticationService = mock(IAuthService.class);

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
            when(authenticationService.register(anyString(), anyString(), anyString(), anyBoolean())).thenThrow(new RuntimeException(""));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        doRegistration.doPost(request, response);

        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    @Test
    void testDoGet() throws IOException, ServletException {
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

    public static void mockParams(String mail, String usrn, String pwd, String pwdConf, HttpServletRequest request) throws IOException {

        boolean putComma = false;
        String json = "{";
        if (mail != null) {
            json += "\"" + MAIL + "\": \"" + mail + "\"";
            putComma = true;
        }
        if (usrn != null) {
            if (putComma)
                json += ", ";
            json += "\"" + USERNAME + "\": \"" + usrn + "\"";
            putComma = true;
        }
        if (pwd != null) {
            if (putComma)
                json += ", ";
            json += "\"" + PASSWORD + "\": \"" + pwd + "\"";
            putComma = true;
        }
        if (pwdConf != null) {
            if (putComma)
                json += ", ";
            json += "\"" + CONF_PASSWORD + "\": \"" + pwdConf + "\"";
        }
        json += "}";

        Reader inputString = new StringReader(json);
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
    }

}