package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.entities.Questionnaire;
import it.polimi.db2.coolsurveys.entities.Submission;
import it.polimi.db2.coolsurveys.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SurveyServiceBean implements SurveyService{
    @Override
    public LocalDate createSurvey(Map<String, List<String>> survey) {
        return null;
    }

    @Override
    public List<Questionnaire> getSurveyList() {
        return null;
    }

    @Override
    public void deleteSurveys(List<LocalDate> dates) {

    }

    @Override
    public List<User> getSurveyResponders(LocalDate date, boolean hasSubmitted) {
        return null;
    }

    @Override
    public Submission getSurveySubmission(LocalDate date, String username) {
        return null;
    }

    @Override
    public Map<String, String> getSurveyAnswers(LocalDate date, String username) {
        return null;
    }

    @Override
    public boolean checkDateAvailability(LocalDate date) {
        return false;
    }

    @Override
    public byte[] getImage() {
        return new byte[0];
    }
}
