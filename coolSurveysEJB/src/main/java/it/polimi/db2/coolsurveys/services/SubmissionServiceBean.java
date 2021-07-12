package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.AnswerDAO;
import it.polimi.db2.coolsurveys.dao.QuestionnaireDAO;
import it.polimi.db2.coolsurveys.entities.*;
import it.polimi.db2.coolsurveys.dao.SubmissionDAO;
import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.services.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Question;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class SubmissionServiceBean implements SubmissionService {

    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    protected QuestionnaireDAO questionnaireDAO;

    @EJB(name = "it.polimi.db2.coolsurveys.dao/AnswerDAO")
    protected AnswerDAO answerDAO;

    @EJB
    protected UserDAO userDAO;

    @EJB(name = "it.polimi.db2.coolsurveys.dao/SubmissionDAO")
    protected SubmissionDAO submissionDAO;

    @PersistenceContext(unitName = "coolSurveys")
    protected EntityManager em;

    protected SubmissionServiceBean(QuestionnaireDAO questionnaireDAO, AnswerDAO answerDAO, UserDAO userDAO, SubmissionDAO submissionDAO) {
        this.questionnaireDAO = questionnaireDAO;
        this.answerDAO = answerDAO;
        this.userDAO = userDAO;
        this.submissionDAO = submissionDAO;
    }

    public SubmissionServiceBean() {}

    /**
     * {@inheritDoc}
     * @param credentials
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Questionnaire retrieveDailySurvey(Credentials credentials) throws NotFoundException, AlreadyExistsException, BlockedAccountException {

        User user = credentials.getUser();

        LocalDateTime blockedUntil = user.getBlockedUntil();

        if(blockedUntil.isAfter(LocalDateTime.now()))
            throw new BlockedAccountException(blockedUntil);

        Questionnaire questionnaire = questionnaireDAO.getByDate(LocalDate.now());

        Submission submission = submissionDAO.find(user, questionnaire);

        if(submission == null) //first time user sees Daily Survey
            submissionDAO.create(user, questionnaire);
        else if(submission.getSubmitted().equals(Boolean.TRUE))
            throw new AlreadyExistsException("User already submitted");



        return questionnaire;

    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerSubmission(Map<String, String> answers, Credentials credentials, Integer age, String gender, String expLvl) throws BlockedAccountException, AlreadyExistsException, NotFoundException, BadWordFoundException {

        if(answers == null || credentials == null)
            throw new IllegalArgumentException();

        Questionnaire questionnaire = questionnaireDAO.getByDate(LocalDate.now());

        User user = credentials.getUser();

        Submission submission = null;

        //Search if user has started a submission
        for(Submission sub : user.getSubmission())
            if(sub.getQuestionnaire().equals(questionnaire)) {
                submission = sub;
                break;
            }

        if(submission == null)
            throw new NotFoundException("User never retrieved the Questionnaire");

        if(submission.getSubmitted().equals(Boolean.TRUE))
            throw new AlreadyExistsException("User already submitted this Questionnaire");

        try {
            insertAnswers(questionnaire, answers, user);
        } catch (it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException e) {
            /*
                At this point the transaction should be marked for rollback as BadWordFoundException is
                annotated with @ApplicationException(rollback = true)
             */
            System.out.println("User inserted a swear word.");
            throw new BadWordFoundException("User inserted a swear word.");
        }

        //Converts to null values if strings are empty ("")
        Integer genderOpt = gender.isEmpty() ? null : Submission.Gender.valueOf(gender.toUpperCase()).ordinal();
        Integer expLvlOpt = expLvl.isEmpty() ? null : Submission.ExpertiseLevel.valueOf(expLvl.toUpperCase()).ordinal();

        submissionDAO.submit(submission, age, genderOpt, expLvlOpt);

    }

    private void insertAnswers(Questionnaire questionnaire, Map<String, String> answers, User user) throws AlreadyExistsException, it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException, BlockedAccountException, NotFoundException {

        //check if User is banned
        if(user.getBlockedUntil().isAfter(LocalDateTime.now()))
            throw new BlockedAccountException();

        for(Question question : questionnaire.getQuestions())
            answerDAO.insertAnswer(question, answers.get(question.getQuestion()), user);
    }

    public void blockUser(Credentials credentials) {
        User user = credentials.getUser();

        userDAO.banUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getLeaderboard() {
        return userDAO.getLeaderBoard();
    }

}
