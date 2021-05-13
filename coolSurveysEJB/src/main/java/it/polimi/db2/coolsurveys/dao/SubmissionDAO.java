package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

@Stateless
public class SubmissionDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public SubmissionDAO(EntityManager em) {
        this.em = em;
    }

    public SubmissionDAO() {}

    public Submission insert(User user, Questionnaire questionnaire, Integer age, Integer isFemale, Integer expLvl) throws BlockedAccountException, AlreadyExistsException {
        if(user == null || questionnaire == null)
            throw new IllegalArgumentException();

        LocalDateTime banTime = user.getBlockedUntil();

        if(banTime.isAfter(LocalDateTime.now()))
            throw new BlockedAccountException(banTime);


        if(em.createNamedQuery("Submission.get", Submission.class).setParameter("userId", user.getCredentials().getUser_id())
            .setParameter("questionnaireId", questionnaire.getQId()).getResultList().size() > 0)
            throw new AlreadyExistsException("User already submitted");

        //Wrap possible null values into Optional containers
        Optional<Integer> ageOpt = Optional.ofNullable(age);
        Optional<Integer> isFemaleOpt = Optional.ofNullable(isFemale);
        Optional<Integer> expLvlOpt = Optional.ofNullable(expLvl);

        Submission submission = new Submission(user, questionnaire);

        ageOpt.ifPresent(submission::setAge);
        isFemaleOpt.ifPresent(submission::setSex);
        expLvlOpt.ifPresent(submission::setExpertiseLevel);

        em.persist(submission);

        return submission;
    }

}
