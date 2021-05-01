package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getLeaderboard() {
        List<User> leaderboard = new ArrayList<>();
        leaderboard.add(new User(new Credentials("username", "pwdHash", "mail", false)));
        return leaderboard;
    }
}
