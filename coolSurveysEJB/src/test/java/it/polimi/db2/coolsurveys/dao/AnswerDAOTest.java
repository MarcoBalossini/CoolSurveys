package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class AnswerDAOTest extends DAOTest {

    static protected QuestionnaireDAO questionnaireDAO = new QuestionnaireDAO(em);
    static protected AnswerDAO answerDAO = new AnswerDAO(em);
    static protected UserDAO userDAO = new UserDAO(em);

    private static final String QUESTIONNAIRE = "questionaire_answer_test";
    private static final String USER = "user";
    private static final String PASSWORD = "pass";
    private static final String MAIL = "user@user.com";
    private static final String TEXT1 = "my answer";
    private static final String TEXT2 = "my modified answer";
    private static final String BAD_TEXT = "bitch";


    @Test
    public void addAnswer() {
        em.getTransaction().begin();

        byte[] smallPhoto = {0, 0, 0};

        List<Question> questionList = new ArrayList<>();

        questionList.add(new Question("question1?"));

        try {

            Questionnaire q = questionnaireDAO.insertQuestionnaire(LocalDate.now(), QUESTIONNAIRE, smallPhoto, questionList);

            answerDAO.insertAnswer(q.getQuestions().get(0), TEXT1, userDAO.insertUser(USER, PASSWORD, MAIL, false));
            em.getTransaction().commit();

        } catch (AlreadyExistsException | BadWordFoundException e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            fail();
        }
    }

    @Test
    public void badWordTest() {
        em.getTransaction().begin();

        byte[] smallPhoto = {0, 0, 0};

        List<Question> questionList = new ArrayList<>();

        questionList.add(new Question("question1?"));

        try {

            Questionnaire q = questionnaireDAO.insertQuestionnaire(LocalDate.now(), QUESTIONNAIRE, smallPhoto, questionList);

            assertThrows(BadWordFoundException.class, () -> answerDAO.insertAnswer(q.getQuestions().get(0), BAD_TEXT, userDAO.insertUser(USER, PASSWORD, MAIL, false)));
            em.getTransaction().rollback();



        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            fail();
        }


    }

    @BeforeEach
    @AfterEach
    public void clean() {
        em.getTransaction().begin();

        em.createNativeQuery("delete from questionnaire").executeUpdate();
        em.createNativeQuery("delete from user").executeUpdate();
        em.createNativeQuery("delete from credentials").executeUpdate();

        em.getTransaction().commit();
    }

}
