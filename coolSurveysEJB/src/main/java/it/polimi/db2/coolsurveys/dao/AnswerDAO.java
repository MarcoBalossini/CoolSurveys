package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.entities.Answer;
import it.polimi.db2.coolsurveys.entities.BadWord;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AnswerDAO {

    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public AnswerDAO(EntityManager em) {
        this.em = em;
    }

    public AnswerDAO() {}

    public Answer insertAnswer(Question question, String text, User user) throws AlreadyExistsException, BadWordFoundException {

        if(question == null || text == null || user == null || text.isEmpty())
            throw new IllegalArgumentException();

        Answer answer;

        if(em.createNamedQuery("Answer.selectByUserAndQuestion", Answer.class)
                .setParameter("user", user)
                .setParameter("question", question).getResultList().size() > 0)
            throw new AlreadyExistsException("User already submitted");

        List<String> badWords = em.createNamedQuery("BadWord.findAllWords", String.class).getResultList();

        for (String word: text.split(" "))
            if (badWords.contains(word)) {
                throw new BadWordFoundException();
            }

        answer = new Answer(question, user, text);

        em.persist(answer);

        em.flush();

        return answer;
    }

}
