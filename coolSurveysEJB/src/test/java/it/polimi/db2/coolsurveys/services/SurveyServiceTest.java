package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.QuestionnaireDAO;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.services.exceptions.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SurveyServiceTest {

    private static QuestionnaireDAO questionnaireDAO;

    private static Questionnaire questionnaire1 = new Questionnaire();
    private static LocalDate yesterday = LocalDate.now().minusDays(1);
    private static LocalDate tomorrow = LocalDate.now().plusDays(1);
    private static LocalDate today = LocalDate.now();

    private static SurveyServiceBean surveyService = new SurveyServiceBean();


    @BeforeAll
    public static void setup() throws NotFoundException {
        questionnaireDAO = mock(QuestionnaireDAO.class);

        when(questionnaireDAO.getByDate(yesterday)).thenReturn(questionnaire1);

        surveyService.questionnaireDAO = questionnaireDAO;

    }

    @Test
    void testDeleteSurveysDates() {
        assertThrows(ServiceException.class, () -> surveyService.deleteSurveys(List.of(today)));
        assertThrows(ServiceException.class, () -> surveyService.deleteSurveys(List.of(tomorrow)));
        assertThrows(ServiceException.class, () -> surveyService.deleteSurveys(List.of(today, tomorrow)));
        assertThrows(ServiceException.class, () -> surveyService.deleteSurveys(List.of(yesterday, today)));
        assertThrows(ServiceException.class, () -> surveyService.deleteSurveys(List.of(yesterday, tomorrow)));
        assertThrows(ServiceException.class, () -> surveyService.deleteSurveys(List.of(yesterday, tomorrow, today)));

        assertDoesNotThrow(() -> surveyService.deleteSurveys(List.of(yesterday)));

    }
}
