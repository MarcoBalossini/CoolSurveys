package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;

import java.util.Map;

public interface ISurveysService {

    /**
     * Retrieve the daily survey's data
     * @return The survey, if found. null otherwise
     * @param username
     */
    // TODO: Add thrown exceptions after implementation
    Questionnaire retrieveDailySurvey(String username) throws AlreadyExistsException, BlockedAccountException;

    void insertAnswers(Map<Question, String> answers, User user) throws BlockedAccountException;

    void submit(Submission submission) throws BlockedAccountException;

}
