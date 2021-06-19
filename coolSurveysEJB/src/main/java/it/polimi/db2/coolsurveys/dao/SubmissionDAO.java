package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
public class SubmissionDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public SubmissionDAO(EntityManager em) {
        this.em = em;
    }

    public SubmissionDAO() {}

    public void submit(Submission submission, Integer age, Integer isFemale, Integer expLvl) throws BlockedAccountException, AlreadyExistsException {
        if(submission == null)
            throw new IllegalArgumentException();

        LocalDateTime banTime = submission.getUser().getBlockedUntil();

        if(banTime.isAfter(LocalDateTime.now()))
            throw new BlockedAccountException(banTime);


        if(submission.getSubmitted().equals(Boolean.TRUE))
            throw new AlreadyExistsException("User already submitted the questionnaire");


        //Wrap possible null values into Optional containers
        Optional<Integer> ageOpt = Optional.ofNullable(age);
        Optional<Integer> isFemaleOpt = Optional.ofNullable(isFemale);
        Optional<Integer> expLvlOpt = Optional.ofNullable(expLvl);

        ageOpt.ifPresent(submission::setAge);
        isFemaleOpt.ifPresent(submission::setSex);
        expLvlOpt.ifPresent(submission::setExpertiseLevel);

        submission.setSubmitted(true);

        em.merge(submission);

    }

    public Submission find(User user, Questionnaire questionnaire) {

        if(user == null || questionnaire == null)
            throw new IllegalArgumentException();

        List<Submission> submissions = em.createNamedQuery("Submission.get", Submission.class).setParameter("userId", user.getCredentials().getUser_id()).setParameter("questionnaireId", questionnaire.getQId())
            .getResultList();

        if(submissions.isEmpty())
            return null;

        return submissions.get(0);
    }

    public Submission create(User user, Questionnaire questionnaire) {
        if(user == null || questionnaire == null)
            throw new IllegalArgumentException();

        Submission submission = new Submission(user, questionnaire);
        em.persist(submission);

        return submission;

    }

    public List<User> getResponders(Questionnaire questionnaire, boolean hasSubmitted) {
        return em.createNamedQuery("Submission.getResponders", User.class)
                .setParameter("questionnaireId", questionnaire.getQId())
                .setParameter("hasSubmitted", hasSubmitted)
                .getResultList();
    }

}
