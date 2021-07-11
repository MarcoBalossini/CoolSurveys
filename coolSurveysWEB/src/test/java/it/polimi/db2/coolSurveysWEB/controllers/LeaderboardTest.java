package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.IUserService;
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

class LeaderboardTest {

    @Test
    void testGetLeaderboard() throws NoSuchFieldException, IllegalAccessException, ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        IUserService userService = mock(IUserService.class);

        Leaderboard leaderboard = new Leaderboard();
        Field userServiceField = leaderboard.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(leaderboard, userService);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        List<User> users = new ArrayList<>();
        users.add(new User(new Credentials("username1", "pwdhash", "mail", false)));
        users.add(new User(new Credentials("username2", "pwdhash", "mail", false)));
        String json = "[" +
                            "{\"username\":\"username1\",\"points\":0}," +
                            "{\"username\":\"username2\",\"points\":0}" +
                        "]";

        when(userService.getLeaderboard()).thenReturn(users);

        leaderboard.doGet(request, response);

        writer.flush();


        assertEquals(stringWriter.toString().trim(), json.trim());
    }

}