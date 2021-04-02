package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.QuestionPK;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireDAOTest extends DAOTest {

    static Questionnaire questionnaire;

    @Test
    void insertQuestionnaire() {
        em.getTransaction().begin();

        QuestionnaireDAO dao = new QuestionnaireDAO(em);

        byte[] smallPhoto = {0, 0, 0};


        List<String> questionList = List.of("question1?", "question2?");

        try {
            questionnaire = dao.insertQuestionnaire("questionnaire1", smallPhoto, questionList);
            em.getTransaction().commit();

        } catch (AlreadyExistsException e) {
            System.out.println("Questionnaire alreay exists");
            em.getTransaction().rollback();
            fail();
        }

    }

    @Test
    void deleteQuestionnaire() {
        insertQuestionnaire();

        //em.clear();

        em.getTransaction().begin();

        Questionnaire questionnaire = em.createNamedQuery("Questionnaire.selectByName", Questionnaire.class).setParameter("name", "questionnaire1").getSingleResult();
        em.remove(questionnaire);

        em.getTransaction().commit();
    }


    @Override
    @BeforeEach
    @AfterEach
    public void clean() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();


        em.getTransaction().begin();
        em.createNativeQuery("delete from questionnaire").executeUpdate();
        em.getTransaction().commit();
    }
}
