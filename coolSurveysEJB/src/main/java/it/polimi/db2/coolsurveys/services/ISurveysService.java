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
    void registerSubmission(Map<String, String> answers, Credentials user, Integer age, String gender, String expLvl) throws BlockedAccountException, AlreadyExistsException, NotFoundException, BadWordFoundException;

    void blockUser(Credentials credentials);

    /**
     * Create a survey in the database for the first available day.
     * @param survey The Map< Questions, List< Options > >
     * @return The planned date
     */
    LocalDate createSurvey(Map<String, List<String>> survey);

    /**
     * Get the list of past surveys
     * @return
     */
    List<Questionnaire> getSurveyList();

    /**
     * Deletes some survey given their dates<br>
     * Controls that all the surveys are already closed
     *
     * @param dates The survey's dates
     */
    void deleteSurveys(List<LocalDate> dates);

    /**
     * Get the list of all the users who had submitted/cancelled a given survey
     * @param date The survey date
     * @param hasSubmitted Which user category to select: submitters or "cancellers"
     * @return The filtered list of user
     */
    List<User> getSurveyResponders(LocalDate date, boolean hasSubmitted);

    /**
     * Get submission data given survey date and responder's username
     * @param date The survey's date
     * @param username The responder's username
     * @return The submission object
     */
    Submission getSurveySubmission(LocalDate date, String username);

    /**
     * Get the Map(Question, Answer) to a given date survey by a selected user
     * @param date The survey's date
     * @param username The submitter's username
     * @return The Question/Answers map
     */
    Map<String, String> getSurveyAnswers(LocalDate date, String username);

    /**
     * Checks whether or not a date is available for a survey
     * @param date The date
     * @return true if the date is available, false id not
     */
    boolean checkDateAvailability(LocalDate date);

    /**
     * Gets the product's of the day image
     * @return The image as a array of bytes
     */
    byte[] getImage();
}
