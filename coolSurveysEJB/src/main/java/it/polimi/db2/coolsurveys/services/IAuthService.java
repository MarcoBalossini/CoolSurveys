package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.services.exceptions.InvalidCredentialsException;

public interface IAuthService {


    /**
     * Check that given credentials are correct
     *
     * @param username The username
     * @param password The password
     * @throws InvalidCredentialsException if the username, password pair is wrong
     * @return The corresponding Credentials object.
     */
    Credentials checkCredentials(String username, String password) throws InvalidCredentialsException;

    /**
     * Find a user by its ID
     *
     * @param id The user id
     * @return The corresponding Credentials object, if existing. Elsewhere null.
     * @throws NotFoundException When a database error occurs
     */
    Credentials tokenLogin(int id) throws NotFoundException;

    /**
     * Register a user in the database, if possible
     *
     * @param mail The user's mail
     * @param username The chosen username
     * @param password The chosen password
     * @param isAdmin Whether the user is going to be an admin or not.
     * @throws AlreadyExistsException when the username already exists in the database
     * @return The newly created credentials, if existing. Elsewhere null.
     */
    Credentials register(String mail, String username, String password, boolean isAdmin) throws AlreadyExistsException;
}
