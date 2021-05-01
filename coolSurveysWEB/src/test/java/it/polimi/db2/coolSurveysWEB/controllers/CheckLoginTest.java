package it.polimi.db2.coolSurveysWEB.controllers;

import it.polimi.db2.coolSurveysWEB.auth.AuthManager;
import it.polimi.db2.coolSurveysWEB.auth.exceptions.TokenException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.IAuthService;
import it.polimi.db2.coolsurveys.services.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static it.polimi.db2.coolSurveysWEB.controllers.CheckLogin.*;


class CheckLoginTest {

    private static final String TOKEN_EXCEPTION = "TokenException";

    @Test
    public void testFormLogin() throws Exception {

        //Test null String value
        testFormNoDB(null, "pwd", "Missing credential value");

        //Test empty String value
        testFormNoDB("", "pwd", "Missing credential value");

        //Test wrong credentials
        testFormMockDB("usrn", "pwd", "Invalid credentials", null, false);

        //Test correct credentials
        Credentials credentials = new Credentials("a", "b", "c", false);
        credentials.setUserId(1);
        testFormMockDB("usrn", "pwd", credentials.getUsername(), credentials, false);

        //Test catch
        testFormMockDB("usrn", "pwd", "Operation not allowed", credentials, true);
    }

    @Test
    public void testTokenLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Credentials credentials = new Credentials("a", "b", "c", false);
        credentials.setUserId(1);
        Cookie[] cookiesNoAuth = createTokenCookie(credentials, response, false);
        Cookie[] cookiesWithAuth = createTokenCookie(credentials, response, true);

        // Test no cookies
        testTokenNoDB(request, response, null, "No cookies found");

        // Test wrong cookies
        testTokenNoDB(request, response, cookiesNoAuth, "Auth cookie not found");

        // Test successful login
        testTokenMockDB(request, response, cookiesWithAuth, credentials.getUsername(), 0, credentials);

        // Test NotFoundException
        testTokenMockDB(request, response, cookiesWithAuth, "User not found", 1, credentials);

        // Test Exception
        testTokenMockDB(request, response, cookiesWithAuth, "Invalid token. Login again", 2, credentials);
    }

    /*
        thrownExc:
            0   : No exception
            1   : TokenException
            else: Exception
     */
    private void testTokenMockDB(HttpServletRequest request, HttpServletResponse response,
                               Cookie[] cookies, String msg, int thrownExc, Credentials credentials) {
        IAuthService authenticationService = mock(IAuthService.class);
        HttpSession session = mock(HttpSession.class);

        try {

            //Use Reflection on check login class to mock injected service
            CheckLogin checkLogin = new CheckLogin();
            Field authServiceField = checkLogin.getClass().getDeclaredField("authService");
            authServiceField.setAccessible(true);
            authServiceField.set(checkLogin, authenticationService);

            //Mocks' responses
            when(request.getSession()).thenReturn(session);
            when(request.getCookies()).thenReturn(cookies);

            if (thrownExc == 0)
                when(authenticationService.tokenLogin(anyInt())).thenReturn(credentials);
            else if (thrownExc == 1)
                when(authenticationService.tokenLogin(anyInt())).thenThrow(new NotFoundException("User not found"));
            else
                when(authenticationService.tokenLogin(anyInt())).thenThrow(new RuntimeException(""));

            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(writer);

            checkLogin.doGet(request, response);
            writer.flush();

            assertTrue(stringWriter.toString().contains(msg));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void testTokenNoDB(HttpServletRequest request, HttpServletResponse response,
                               Cookie[] cookies, String msg) throws IOException, ServletException {
        when(request.getCookies()).thenReturn(cookies);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new CheckLogin().doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    private void testFormNoDB(String usrn, String pwd, String msg) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        mockParams(usrn, pwd, request);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new CheckLogin().doPost(request, response);

        writer.flush();

        assertTrue(stringWriter.toString().contains(msg));
    }

    private void testFormMockDB(String usrn, String pwd, String msg, Credentials credentials, boolean throwsRuntimeException) {
        //Create mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        IAuthService authenticationService = mock(IAuthService.class);

        try {

            //Use Reflection on check login class to mock injected service
            CheckLogin checkLogin = new CheckLogin();
            Field authServiceField = checkLogin.getClass().getDeclaredField("authService");
            authServiceField.setAccessible(true);
            authServiceField.set(checkLogin, authenticationService);

            //Mocks' responses
            mockParams(usrn, pwd, request);
            when(request.getSession()).thenReturn(session);

            if(throwsRuntimeException)
                when(authenticationService.checkCredentials(anyString(), anyString())).thenThrow(new RuntimeException(""));
            else if(credentials == null)
                when(authenticationService.checkCredentials(anyString(), anyString())).thenThrow(new InvalidCredentialsException());
            else
                when(authenticationService.checkCredentials(anyString(), anyString())).thenThrow(new RuntimeException(""));


            StringWriter stringWriter = new StringWriter();

            PrintWriter writer = new PrintWriter(stringWriter);

            when(response.getWriter()).thenReturn(writer);
            checkLogin.doPost(request, response);

            writer.flush();

            assertTrue(stringWriter.toString().contains(msg));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }

    private Cookie[] createTokenCookie(Credentials credentials, HttpServletResponse response, boolean withAuth) throws IOException {
        AuthManager authManager = AuthManager.getInstance();
        String token = authManager.generateToken(credentials.getUser_id(), EXPIRATION_TIME);
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Cannot generate token");
            return null;
        }

        Cookie cookie;

        if (withAuth)
            cookie = new Cookie(AUTH_TOKEN, token);
        else
            cookie = new Cookie("name", token);

        cookie.setMaxAge(24*60*60);
        response.addCookie(cookie);

        return new Cookie[]{cookie};
    }

    public static void mockParams(String usrn, String pwd, HttpServletRequest request) throws IOException {

        boolean putComma = false;
        String json = "{";
        if (usrn != null) {
            json += "\"" + USERNAME + "\": \"" + usrn + "\"";
            putComma = true;
        }
        if (pwd != null) {
            if (putComma)
                json += ", ";
            json += "\"" + PASSWORD + "\": \"" + pwd + "\"";
            putComma = true;
        }
        json += "}";

        Reader inputString = new StringReader(json);
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
    }

}