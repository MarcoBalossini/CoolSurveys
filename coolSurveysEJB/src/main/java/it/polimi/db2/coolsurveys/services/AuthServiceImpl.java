package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class AuthServiceImpl implements AuthService {

    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    protected UserDAO dataAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials checkCredentials(String username, String password) throws Exception {
        return new Credentials();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials register(String mail, String username, String password, boolean isAdmin) {
        return new Credentials();
    }
}
