package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.entities.Question;
import com.google.gson.JsonObject;
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

    /**
     * Register a survey submission
     * @param questionAnswerMap The map mapping answers to questions
     * @param age The user age
     * @param sex The user sex
     * @param expLvl The user expertise level
     */
    void registerSubmission(JsonObject questionAnswerMap, int age, String sex, String expLvl);

}
