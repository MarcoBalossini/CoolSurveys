package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.entities.Credentials;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class AuthService implements IAuthService {

    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    protected UserDAO dataAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials checkCredentials(String username, String password) throws Exception {
        return new Credentials(1,"username",null,"ciao@user", false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials tokenLogin(int id) throws Exception {
        return new Credentials(1,"username",null,"ciao@user", false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials register(String mail, String username, String password, boolean isAdmin) throws Exception {
        return new Credentials(1,"username",null,"ciao@user", false);
    }
}
