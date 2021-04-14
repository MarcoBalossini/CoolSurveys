package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public QuestionnaireDAO(EntityManager em) {
        this.em = em;
    }

    public Questionnaire insertQuestionnaire (String name, byte[] photo, List<Question> questionList) throws AlreadyExistsException {

        if(name == null || photo == null || questionList == null || name.isEmpty() || questionList.isEmpty() || photo.length == 0)
            throw new IllegalArgumentException();

        if (em.createNamedQuery("Questionnaire.selectByName")
                .setParameter("name", name).getResultList().size() > 0)
            throw new AlreadyExistsException("Questionnaire already exists");

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(name);
        questionnaire.setPhoto(photo);

        for(Question q : questionList) {
            q.setQuestionnaire(questionnaire);
            questionnaire.addQuestion(q);
        }

        em.persist(questionnaire);

        for(Question q: questionnaire.getQuestions())
            em.persist(q);

        return questionnaire;
    }

    public Questionnaire getByName(String name) throws NotFoundException {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();

        try {
            return em.createNamedQuery("Questionnaire.selectByName", Questionnaire.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Questionnaire \"" + name + "\" not found");
        }
    }

    public void removeQuestionnaire(Questionnaire questionnaire) {

        em.remove(questionnaire);
    }

}
