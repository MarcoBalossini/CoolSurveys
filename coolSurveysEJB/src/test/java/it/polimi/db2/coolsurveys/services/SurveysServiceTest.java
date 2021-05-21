package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.AnswerDAO;
import it.polimi.db2.coolsurveys.dao.QuestionnaireDAO;
import it.polimi.db2.coolsurveys.dao.SubmissionDAO;
import it.polimi.db2.coolsurveys.dao.UserDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.services.exceptions.BadWordFoundException;
import it.polimi.db2.coolsurveys.dao.exceptions.BlockedAccountException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class SurveysServiceTest {

    private static final String USER1 = "user1";
    private static final String PASS1 = "pass1";
    private static final String MAIL1 = "mail1";

    private static final String USER2 = "user2";
    private static final String PASS2 = "pass2";
    private static final String MAIL2 = "mail2";

    private static final String QUESTIONNAIRE = "questionnaire";

    private static final String QUESTION1 = "question1";
    private static final String QUESTION2 = "question2";

    private static UserDAO userDAO;
    private static AnswerDAO answerDAO;
    private static SubmissionDAO submissionDAO;
    private static QuestionnaireDAO questionnaireDAO;

    private static SurveysService service;

    private static Credentials cred1;
    private static Credentials cred2;
    private static User user1;
    private static User user2;

    private static Questionnaire questionnaire;
    private static Question question1;

    private static Map<String, String> map;

    @BeforeAll
    public static void setup() throws NotFoundException {
        userDAO = mock(UserDAO.class);
        answerDAO = mock(AnswerDAO.class);
        submissionDAO = mock(SubmissionDAO.class);
        questionnaireDAO = mock(QuestionnaireDAO.class);

        service = new SurveysService(questionnaireDAO, answerDAO, userDAO, submissionDAO);

        cred1 = new Credentials(USER1, PASS1, MAIL1, false);
        user1 = new User(cred1);
        cred1.setUser(user1);


        //blocked user
        cred2 = new Credentials(USER2, PASS2, MAIL2, false);
        user2 = new User(cred2);
        cred2.setUser(user2);

        user2.setBlockedUntil(LocalDateTime.now().plusMonths(1));

        byte[] photo = {};
        questionnaire = new Questionnaire(QUESTIONNAIRE, photo);
        questionnaire.setDate(LocalDate.now());

        question1 = new Question(QUESTION1);
        questionnaire.addQuestion(question1);
        questionnaire.addQuestion(new Question(QUESTION2));
        when(questionnaireDAO.getByDate(LocalDate.now())).thenReturn(questionnaire);


    }

    @Test
    public void blockedAccount() {
        map = Map.of(QUESTION1, "answer1", QUESTION2, "answer2");
        assertThrows(BlockedAccountException.class, () -> service.registerSubmission(map, cred2, 1, "Male", "High"));
    }

    @Test
    public void badWordInsertion() throws NotFoundException, AlreadyExistsException, it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException {
        map = Map.of(QUESTION1, "answer1", QUESTION2, "answer2");

        when(answerDAO.insertAnswer(any(Question.class), anyString(), any(User.class))).thenThrow(new it.polimi.db2.coolsurveys.dao.exceptions.BadWordFoundException());

        assertThrows(BadWordFoundException.class, () -> service.registerSubmission(map, cred1, 1, "Male", "Low"));
    }

    @Test
    public void nullArguments() {
        map = Map.of(QUESTION1, "answer1", QUESTION2, "answer2");

        assertThrows(IllegalArgumentException.class, () -> service.registerSubmission(null, cred1, 1, "Male", "Low"));
        assertThrows(IllegalArgumentException.class, () -> service.registerSubmission(map,null, 1, "Male", "Low"));
        assertThrows(IllegalArgumentException.class, () -> service.registerSubmission(null, null, 1, "Male", "Low"));

    }



}
