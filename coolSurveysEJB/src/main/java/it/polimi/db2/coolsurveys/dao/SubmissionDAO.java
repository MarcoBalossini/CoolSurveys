package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

public class SubmissionDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public SubmissionDAO(EntityManager em) {
        this.em = em;
    }

    public Submission insert(User user, Questionnaire questionnaire) throws BlockedAccountException {
        if(user == null || questionnaire == null)
            throw new IllegalArgumentException();

        LocalDateTime banTime = user.getBlocked_until();

        if(banTime.isAfter(LocalDateTime.now()))
            throw new BlockedAccountException(banTime);


        //TODO: check if this submission already exists? if yes current behaviour erases statistical data
        Submission submission = new Submission(user, questionnaire);

        em.persist(submission);

        return submission;
    }

    public void update(Submission submission) {
        em.merge(submission);
    }

    public void submit(Submission submission) throws BlockedAccountException {

        LocalDateTime banTime = submission.getUser().getBlocked_until();

        if(banTime.isAfter(LocalDateTime.now()))
            throw new BlockedAccountException(banTime);

        submission.setSubmitted(true);
        em.merge(submission);
    }
}