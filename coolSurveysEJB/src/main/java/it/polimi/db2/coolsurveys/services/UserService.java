package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {


    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    private UserDAO dao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getLeaderboard() {
        List<User> leaderboard = new ArrayList<>();



        return leaderboard;
    }
}
