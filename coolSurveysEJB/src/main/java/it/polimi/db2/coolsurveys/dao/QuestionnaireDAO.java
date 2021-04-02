package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireDAO {
    @PersistenceContext
    private EntityManager em;

    public QuestionnaireDAO(EntityManager em) {
        this.em = em;
    }

    public Questionnaire insertQuestionnaire (String name, byte[] photo, List<String> questionList) throws AlreadyExistsException {

        if(name == null || photo == null || questionList == null || name.isEmpty() || questionList.isEmpty() || photo.length == 0)
            throw new IllegalArgumentException();

        if (em.createNamedQuery("Questionnaire.selectByName")
                .setParameter("name", name).getResultList().size() > 0)
            throw new AlreadyExistsException();

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(name);
        questionnaire.setPhoto(photo);

        for(String q : questionList)
            questionnaire.addQuestion(new Question(q, questionnaire));

        em.persist(questionnaire);

        for(Question q: questionnaire.getQuestions())
            em.persist(q);

        return questionnaire;
    }

    public Questionnaire getByName(String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();

        return em.createNamedQuery("Questionnaire.selectByName", Questionnaire.class).setParameter("name", name).getSingleResult();
    }

    public void removeQuestionnaire(Questionnaire questionnaire) {
        for (Question q : questionnaire.getQuestions()) {
            em.remove(q);
        }

        em.remove(questionnaire);
    }

}
