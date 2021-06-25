package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.PersistenceTest;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Option;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireDAOTest extends PersistenceTest {


    @Test
    void insertQuestionnaire() {
        em.getTransaction().begin();

        Questionnaire questionnaire = null;

        QuestionnaireDAO dao = new QuestionnaireDAO(em);


        byte[] smallPhoto = {0, 0, 0};

        List<Question> questionList = new ArrayList<>();
        List<Review> reviewList = new ArrayList<>();

        questionList.add(new Question("question1?"));
        questionList.add(new Question("question2?"));

        reviewList.add(new Review("Review 1"));
        reviewList.add(new Review("Review 2"));

        try {
            questionnaire = dao.insertQuestionnaire(LocalDate.now(), "questionnaire1", smallPhoto, questionList);
            em.getTransaction().commit();

        } catch (AlreadyExistsException e) {
            System.out.println("Questionnaire alreay exists");
            em.getTransaction().rollback();
            fail();
        }

        try {
            em.getTransaction().begin();
            
            questionnaire.setReviews(reviewList);
            
            em.getTransaction().commit();

        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
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
            Questionnaire questionnaire = dao.insertQuestionnaire(LocalDate.now(), "questionnaire1", smallPhoto, List.of(q1));
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
