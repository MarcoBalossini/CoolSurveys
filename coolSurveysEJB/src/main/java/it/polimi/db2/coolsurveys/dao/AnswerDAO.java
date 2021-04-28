package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.entities.Answer;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class AnswerDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public AnswerDAO(EntityManager em) {
        this.em = em;
    }

    public Answer insertAnswer(Question question, String text, User user) {


        if(question == null || text == null || user == null || text.isEmpty())
            throw new IllegalArgumentException();

        Answer answer;

        List<Answer> results = em.createNamedQuery("Answer.selectByUserAndQuestion", Answer.class)
                .setParameter("user", user)
                .setParameter("question", question).getResultList();

        //if the answer does exists it just updates it
        if(results.isEmpty())
            answer = new Answer(question, user, text);
        else {
            answer = results.get(0);
            answer.setAnswer(text);
        }

        em.persist(answer);

        em.flush();

        return answer;
    }

}
