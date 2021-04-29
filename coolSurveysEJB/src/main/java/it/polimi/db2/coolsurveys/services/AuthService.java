package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;

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
    public Credentials checkCredentials(String username, String password) {
        User user = null;
        try {
            user = dataAccess.retrieveUserByUsername(username);
        } catch (NotFoundException e) {
            return null;
        }

        Credentials credentials = user.getCredentials();

        return credentials.getPassword_hash().equals(password) ? credentials : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials tokenLogin(int id) throws Exception {
        User u = dataAccess.find(id);

        if(u != null)
            return u.getCredentials();

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials register(String mail, String username, String password, boolean isAdmin)  {

        Credentials credentials;

        try {
            credentials = dataAccess.insertUser(username, password, mail, isAdmin).getCredentials();
        } catch (AlreadyExistsException e) {
            credentials = null;
        }

        return credentials;

    }
}
