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

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Questionnaire retrieveDailySurvey() throws NotFoundException {

        return questionnaireDAO.getByDate(LocalDate.now());

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertAnswers(Map<String, String> answers, Credentials credentials, Integer age, String gender, String expLvl) throws BlockedAccountException, BadWordFoundException, AlreadyExistsException, NotFoundException {

        if(answers == null || credentials == null)
            throw new IllegalArgumentException();

        User user = credentials.getUser();

        Questionnaire questionnaire = questionnaireDAO.getByDate(LocalDate.now());

        //check if User is banned
        if(user.getBlockedUntil().isAfter(LocalDateTime.now()))
            throw new BlockedAccountException();

        List<Question> questions = questionnaire.getQuestions();

        try {
            for (Question question : questions)
                answerDAO.insertAnswer(question, answers.get(question.getQuestion()), user);
        } catch (BadWordFoundException e) {

            //userDAO.banUser(user);
            throw new BadWordFoundException();
        }

        //Converts to null values if strings are empty ("")
        Integer genderOpt = gender.isEmpty() ? null : Submission.Gender.valueOf(gender.toUpperCase()).ordinal();
        Integer expLvlOpt = expLvl.isEmpty() ? null : Submission.ExpertiseLevel.valueOf(expLvl.toUpperCase()).ordinal();

        submissionDAO.insert(user, questionnaire, age, genderOpt, expLvlOpt);

    }
    }
