package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.QuestionnaireDAO;
import it.polimi.db2.coolsurveys.dao.SubmissionDAO;
import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Answer;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;
import it.polimi.db2.coolsurveys.services.exceptions.ServiceException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class SurveyServiceBean implements SurveyService{

    @EJB(name = "it.polimi.db2.coolsurveys.dao/QuestionnaireDAO")
    protected QuestionnaireDAO questionnaireDAO;

    @EJB(name = "it.polimi.db2.coolsurveys.dao/SubmissionDAO")
    protected SubmissionDAO submissionDAO;

    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    protected UserDAO userDAO;

    @PersistenceContext(unitName = "coolSurveys")
    protected EntityManager em;


    public SurveyServiceBean() {}

    public SurveyServiceBean(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    @Override
    public void createSurvey(String name, Map<String, List<String>> survey, LocalDate date, byte[] image) {
        return;
    }

    @Override
    public List<Questionnaire> getSurveyList() {
        return null;
    }

    @Override
    public void deleteSurveys(List<LocalDate> dates) throws ServiceException, NotFoundException {
        List<Questionnaire> questionnaires = new ArrayList<>();

        //checks all dates precede current one and retrieves all the associated questionnaires
        for (LocalDate date : dates) {
            if (date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now()))
                throw new ServiceException("Deletion of a survey is possible only for a date preceding the current one");
            questionnaires.add(questionnaireDAO.getByDate(date));
        }

        for (Questionnaire questionnaire : questionnaires) {
            questionnaireDAO.removeQuestionnaire(questionnaire); //Triggers will compute points change for users. Changes won't be immediately reflected in the Persistence Context.
        }


    }

    @Override
    public List<User> getSurveyResponders(LocalDate date, boolean hasSubmitted) throws NotFoundException {
        Questionnaire questionnaire = questionnaireDAO.getByDate(date);

        return questionnaire.getSubmissions()
                .stream()
                .filter(s -> s.getSubmitted() == hasSubmitted)
                .map(Submission::getUser)
                .collect(Collectors.toList());

    }

    @Override
    public Submission getSurveySubmission(LocalDate date, String username) throws NotFoundException{
        Questionnaire questionnaire = questionnaireDAO.getByDate(date);

        User user = userDAO.retrieveUserByUsername(username);

        return submissionDAO.find(user, questionnaire);
    }

    @Override
    public Map<String, String> getSurveyAnswers(LocalDate date, String username) throws NotFoundException {
        Questionnaire questionnaire = questionnaireDAO.getByDate(date);

        User user = userDAO.retrieveUserByUsername(username);

        return user.getAnswers()
                .stream()
                .filter(a -> a.getQuestion().getQuestionnaire().equals(questionnaire))
                .collect(Collectors.toMap(a -> a.getQuestion().getQuestion(), Answer::getAnswer));

    }

    @Override
    public boolean checkDateAvailability(LocalDate date) {
        try {
            questionnaireDAO.getByDate(date);
            return false;
        } catch (NotFoundException e) {
            return true;
        }
    }

    @Override
    public byte[] getImage() throws NotFoundException {
        Questionnaire questionnaire = questionnaireDAO.getByDate(LocalDate.now());

        return questionnaire.getPhoto();

    }

    @Override
    public List<String> getReviews() {
        return new ArrayList<>();
    }
}
