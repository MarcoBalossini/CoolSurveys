package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.entities.Answer;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class AnswerDAO {

    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public AnswerDAO(EntityManager em) {
        this.em = em;
    }

    public AnswerDAO() {}


    /**
     * Insert an answer into the database. If a swear word (@see BadWordFoundException) the transaction is rolled bacl.
     * @param question question to answer
     * @param text text of the answer
     * @param user user who ansers
     * @return the answer instance
     * @throws AlreadyExistsException if the answer has already been submitted
     * @throws BadWordFoundException if the answer contains a swear word. It causes the transaction to be rolled back.
     */

    public Answer insertAnswer(Question question, String text, User user) throws AlreadyExistsException, BadWordFoundException {

        if(question == null || text == null || user == null || text.isEmpty())
            throw new IllegalArgumentException();

        Answer answer;

        if(em.createNamedQuery("Answer.selectByUserAndQuestion", Answer.class)
                .setParameter("user", user)
                .setParameter("question", question).getResultList().size() > 0)
            throw new AlreadyExistsException("User already answered this question");

        List<String> badWords = em.createNamedQuery("BadWord.findAllWords", String.class).getResultList();

        /*for (String word: text.replaceAll("[^a-zA-Z0-9]", " ").split(" "))
            if (badWords.contains(word.toLowerCase()))
                throw new BadWordFoundException(); //this will cause a Rollback (see BadWordFoundException annotation)
        */

        text = text.toLowerCase();

        for (String word : badWords) {
            if (text.contains(word))
                throw new BadWordFoundException();
        }

        answer = new Answer(question, user, text);

        em.persist(answer);

        return answer;
    }

}
