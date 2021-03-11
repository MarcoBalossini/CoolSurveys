package it.polimi.db2.coolsurveys;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class AuthManager {

    @EJB
    protected DataAccessInt dataAccess;

}
