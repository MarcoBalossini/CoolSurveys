package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.services.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.*;
import it.polimi.db2.coolsurveys.entities.Questionnaire;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SubmissionService {

    /**
     * Retrieve the daily survey's data
     * @return The survey, if found. null otherwise
     * @param credentials
     */
    Questionnaire retrieveDailySurvey(Credentials credentials) throws NotFoundException, AlreadyExistsException;

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
    void registerSubmission(Map<String, String> answers, Credentials user, Integer age, String gender, String expLvl) throws BlockedAccountException, AlreadyExistsException, NotFoundException, BadWordFoundException;

    void blockUser(Credentials credentials);


}
