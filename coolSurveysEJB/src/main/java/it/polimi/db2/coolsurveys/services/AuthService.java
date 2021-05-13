package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.services.exceptions.InvalidCredentialsException;
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
    public Credentials checkCredentials(String username, String password) throws InvalidCredentialsException {
        User user;
        try {
            user = dataAccess.retrieveUserByUsername(username);
        } catch (NotFoundException e) {
            throw new InvalidCredentialsException();
        }

        Credentials credentials = user.getCredentials();

        if(credentials.getPassword_hash().equals(password)) {
            dataAccess.log(user);
            return credentials;
        }


        throw new InvalidCredentialsException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials tokenLogin(int id) throws NotFoundException {
        User u = dataAccess.find(id);

        dataAccess.log(u);

        return u.getCredentials();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials register(String mail, String username, String password, boolean isAdmin) throws AlreadyExistsException{

        Credentials credentials;

        credentials = dataAccess.insertUser(username, password, mail, isAdmin).getCredentials();

        return credentials;

    }
}
