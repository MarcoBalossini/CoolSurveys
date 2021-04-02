package it.polimi.db2.coolSurveysWEB.auth;

public interface IAuthManager {

    /**
     *
     * @param hash
     * @return
     */
    boolean checkToken(String hash);

    /**
     *
     * @param usrn
     * @return
     */
    String generateToken(String usrn);

}
