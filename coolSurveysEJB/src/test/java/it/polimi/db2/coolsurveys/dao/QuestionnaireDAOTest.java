package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Option;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireDAOTest extends DAOTest {


    @Test
    void insertQuestionnaire() {
        em.getTransaction().begin();

        QuestionnaireDAO dao = new QuestionnaireDAO(em);

        byte[] smallPhoto = {0, 0, 0};

        List<Question> questionList = new ArrayList<>();

        questionList.add(new Question("question1?"));
        questionList.add(new Question("question2?"));

        try {
            Questionnaire questionnaire = dao.insertQuestionnaire("questionnaire1", smallPhoto, questionList);
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

        QuestionnaireDAO dao = new QuestionnaireDAO(em);

        Questionnaire questionnaire = null;
        try {
            questionnaire = dao.getByName("questionnaire1");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            fail();
        }
        dao.removeQuestionnaire(questionnaire);

        em.getTransaction().commit();
    }

    @Test
    void questionsWithOptions() {
        em.getTransaction().begin();

        QuestionnaireDAO dao = new QuestionnaireDAO(em);

        byte[] smallPhoto = {0,0,0};

        Question q1 = new Question("questionWithOption1?");
        q1.setOptions(List.of(new Option("option1"), new Option("option2")));

        try {
            Questionnaire questionnaire = dao.insertQuestionnaire("questionnaire1", smallPhoto, List.of(q1));
            em.getTransaction().commit();

        } catch (AlreadyExistsException e) {
            System.out.println("Questionnaire already exists");
            em.getTransaction().rollback();
            fail();
        }

    }


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
