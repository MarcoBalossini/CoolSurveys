package it.polimi.db2.coolSurveysWEB.controllers;

import com.google.gson.Gson;
import it.polimi.db2.coolSurveysWEB.utils.ResponseUser;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.IUserService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Leaderboard", value = "/Leaderboard")
public class Leaderboard extends HttpServlet {

    @EJB(name = "it.polimi.db2.coolsurveys.services/UserService")
    private IUserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> leaderboard = userService.getLeaderboard();

        try {
            List<ResponseUser> responseLeaderboard = createResponseList(leaderboard);
            String json = new Gson().toJson(responseLeaderboard);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(json);
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Database error");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("POST method not implemented on this endpoint");
    }

    private List<ResponseUser> createResponseList(List<User> leaderboard) {
        List<ResponseUser> toSend = new ArrayList<>();
        leaderboard.forEach((user) -> toSend.add(new ResponseUser(user.getCredentials().getUsername(), user.getPoints())));
        return toSend;
    }
}
