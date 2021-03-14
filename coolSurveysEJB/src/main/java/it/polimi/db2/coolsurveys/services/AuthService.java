package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.entities.User;

public interface AuthService {
    User checkCredentials(String username, String password);
    User register(String mail, String username, String password, boolean isAdmin);

    //TODO: recupero password tramite email
}
