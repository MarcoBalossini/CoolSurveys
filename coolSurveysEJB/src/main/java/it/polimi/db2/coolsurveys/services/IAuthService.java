package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.entities.Credentials;

public interface IAuthService {


    /**
     * Check that given credentials are correct
     *
     * @param username The username
     * @param password The password
     * @return The corresponding Credentials object, if existing. Elsewhere null.
     * @throws Exception When a database error occurs
     */
    Credentials checkCredentials(String username, String password) throws Exception;

    /**
     * Find a user by its ID
     *
     * @param id The user id
     * @return The corresponding Credentials object, if existing. Elsewhere null.
     * @throws Exception When a database error occurs
     */
    Credentials tokenLogin(int id) throws Exception;

    /**
     * Register a user in the database, if possible
     *
     * @param mail The user's mail
     * @param username The chosen username
     * @param password The chosen password
     * @param isAdmin Whether the user is going to be an admin or not.
     * @return The newly created credentials, if existing. Elsewhere null.
     */
    Credentials register(String mail, String username, String password, boolean isAdmin) throws Exception;

    //TODO: recupero password tramite email
}
