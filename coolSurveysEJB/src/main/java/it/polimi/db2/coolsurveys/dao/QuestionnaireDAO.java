package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.entities.*;

import javax.persistence.EntityManager;
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

        if(name == null || name.isEmpty() || photo == null || questionList == null || questionList.size() <= 0)
            throw new IllegalArgumentException();

        if (em.createNamedQuery("Questionnaire.selectByName")
                .setParameter("name", name).getResultList().size() > 0)
            throw new AlreadyExistsException();

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(name);

        for(Question q : questionList)
            q.setQuestionnaire(questionnaire);

        questionnaire.setQuestions(questionList);
        questionnaire.setPhoto(photo);

        em.persist(questionnaire);


        return questionnaire;
    }

}
