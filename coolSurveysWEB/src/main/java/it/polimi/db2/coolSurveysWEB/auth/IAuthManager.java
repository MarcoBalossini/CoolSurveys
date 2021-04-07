package it.polimi.db2.coolSurveysWEB.auth;

import it.polimi.db2.coolSurveysWEB.auth.exceptions.TokenException;

public interface IAuthManager {

    /**
     * Checks a token validity
     * @param token The token to be checked
     * @return The user id, if the token is valid
     * @throws TokenException When the token is not valid
     */
    int checkToken(String token) throws TokenException;

    /**
     * Generate a token
     * @param id The user id
     * @return The token
     */
    String generateToken(int id, long validity);

}
