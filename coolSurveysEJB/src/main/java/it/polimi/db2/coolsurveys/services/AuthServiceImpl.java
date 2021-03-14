package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class AuthServiceImpl implements AuthService {

    @EJB
    protected UserDAO dataAccess;

    @Override
    public User checkCredentials(String username, String password) {
        return new User();
    }

    @Override
    public User register(String mail, String username, String password, boolean isAdmin) {
        return new User();
    }
}
