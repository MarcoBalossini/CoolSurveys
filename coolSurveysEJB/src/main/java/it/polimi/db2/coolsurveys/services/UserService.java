package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UserService implements IUserService {


    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    private UserDAO dao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getLeaderboard() {
        return dao.getLeaderBoard();
    }
}
