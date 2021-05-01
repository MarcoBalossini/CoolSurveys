package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.AnswerDAO;
import com.google.gson.JsonObject;
import it.polimi.db2.coolsurveys.dao.QuestionnaireDAO;
import it.polimi.db2.coolsurveys.entities.*;
import it.polimi.db2.coolsurveys.dao.SubmissionDAO;
import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Question;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Stateless
public class SurveysService implements ISurveysService {

    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    protected QuestionnaireDAO questionnaireDAO;

    @EJB(name = "it.polimi.db2.coolsurveys.dao/AnswerDAO")
    protected AnswerDAO answerDAO;

    @EJB(name = "it.polimi.db2.coolsuveys.dao/UserDAO")
    protected UserDAO userDAO;

    @EJB(name = "it.polimi.db2.coolsurveys.dao/SubmissionDAO")
    protected SubmissionDAO submissionDAO;

    @PersistenceContext(unitName = "coolSurveys")
    protected EntityManager em;

    /**
     * {@inheritDoc}
     * @param credentials
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Questionnaire retrieveDailySurvey(Credentials credentials) throws AlreadyExistsException, BlockedAccountException, NotFoundException {

        if(credentials  == null || credentials.getUsername() == null || credentials.getUsername().isEmpty())
            throw new IllegalArgumentException();


            Questionnaire questionnaire =  questionnaireDAO.getByDate(LocalDate.now());

            User user = userDAO.find(credentials.getUser_id());

            submissionDAO.insert(user, questionnaire);

            return questionnaire;

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertAnswers(Map<Question, String> answers, User user) throws BlockedAccountException, BadWordFoundException {

        if(answers == null || user == null)
            throw new IllegalArgumentException();


        //check if User is banned
        if(user.getBlockedUntil().isAfter(LocalDateTime.now()))
            throw new BlockedAccountException();

        try {
            for (Question question : answers.keySet())
                answerDAO.insertAnswer(question, answers.get(question), user);
        } catch (PersistenceException e) {
            if(e.getMessage().equals("Bad Word Found")) {
                userDAO.banUser(user);
                throw new BadWordFoundException();
            }
            else throw e;
        }

    }

    @Override
    public void submit(Submission submission) throws BlockedAccountException {
        submissionDAO.submit(submission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSubmission(JsonObject questionAnswerMap, int age, String sex, String expLvl) {
        return;
    }
}
