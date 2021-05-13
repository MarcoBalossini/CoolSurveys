package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.*;
import it.polimi.db2.coolsurveys.entities.Questionnaire;

import java.util.Map;

public interface ISurveysService {

    /**
     * Retrieve the daily survey's data
     * @return The survey, if found. null otherwise
     */
    Questionnaire retrieveDailySurvey() throws NotFoundException;

    /**
     * Submit user's answers
     * @param answers answers to submit
     * @param user user who submits
     * @param age age of the user.
     * @param gender user's gender (can be empty)
     * @param expLvl user's expertise level (can be empty)
     * @throws BlockedAccountException if the user's account has been blocked
     * @throws BadWordFoundException if the user typed some forbidden word.
     * @throws AlreadyExistsException if the user already submitted questionnaire `questionnaire`
     */
    void insertAnswers(Map<String, String> answers, Credentials user, Integer age, String gender, String expLvl) throws BlockedAccountException, BadWordFoundException, AlreadyExistsException, NotFoundException;



}
