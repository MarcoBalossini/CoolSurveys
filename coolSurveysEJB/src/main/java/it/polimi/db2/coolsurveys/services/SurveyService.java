package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SurveyService {
    /**
     * Create a survey in the database for the given date
     * @param name The product's name
     * @param survey The Map< Questions, List< Options > >
     * @param date The survey's date
     * @param image The product's image
     */
    void createSurvey(String name, Map<String, List<String>> survey, LocalDate date, byte[] image) throws AlreadyExistsException;

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
    void deleteSurveys(List<LocalDate> dates) throws ServiceException, NotFoundException;

    /**
     * Get the list of all the users who had submitted/cancelled a given survey
     * @param date The survey date
     * @param hasSubmitted Which user category to select: submitters or "cancellers"
     * @return The filtered list of user
     */
    List<User> getSurveyResponders(LocalDate date, boolean hasSubmitted) throws NotFoundException;

    /**
     * Get submission data given survey date and responder's username
     * @param date The survey's date
     * @param username The responder's username
     * @return The submission object
     */
    Submission getSurveySubmission(LocalDate date, String username) throws NotFoundException;

    /**
     * Get the Map(Question, Answer) to a given date survey by a selected user
     * @param date The survey's date
     * @param username The submitter's username
     * @return The Question/Answers map
     */
    Map<String, String> getSurveyAnswers(LocalDate date, String username) throws NotFoundException;

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
    byte[] getImage() throws NotFoundException;

    /**
     * Gets the product's of the day name
     * @return The name
     */
    String getName() throws NotFoundException;

    /**
     * Get the reviews of today's survey as a list of strings
     * @return The list of reviews
     */
    List<String> getReviews() throws NotFoundException;

    /**
     * Get the number of questions in today's survey
     * @return The number of questions
     * @throws NotFoundException When there's no survey
     */
    int getQuestionCount() throws NotFoundException;
}