package it.polimi.db2.coolSurveysWEB.utils;

public class ResponseUser {

    private String username;
    private Integer points = 0;

    public ResponseUser(String username, int points) {
        this.username = username;
        this.points = points;
    }

}
