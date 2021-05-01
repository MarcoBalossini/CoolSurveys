package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.entities.User;

import java.util.List;

public interface IUserService {

    /**
     * Gets the leaderboard
     * @return
     */
    List<User> getLeaderboard();

}
