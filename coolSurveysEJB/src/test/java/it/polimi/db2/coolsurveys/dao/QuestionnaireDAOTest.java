package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.entities.Question;
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

        byte[] smallPhoto = {0,0,0};

        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question("question1?"));
        questionList.add(new Question("question2?"));


        try {
            dao.insertQuestionnaire("questionnaire1", smallPhoto, questionList);
        } catch (AlreadyExistsException e) {
            fail();
        }

        em.getTransaction().commit();

    }


    @Override
    @BeforeEach
    @AfterEach
    public void clean() {
        em.getTransaction().begin();
        em.createNativeQuery("delete from questionnaire").executeUpdate();
        em.getTransaction().commit();
    }
}
