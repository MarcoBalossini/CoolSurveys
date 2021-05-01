package it.polimi.db2.coolSurveysWEB.utils;

import it.polimi.db2.coolsurveys.entities.Answer;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.Submission;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class ResponseUser {

    private String username;
    private Integer points = 0;

    public ResponseUser(String username, int points) {
        this.username = username;
        this.points = points;
    }

}
