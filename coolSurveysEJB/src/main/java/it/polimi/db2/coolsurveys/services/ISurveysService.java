package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.entities.Questionnaire;

public interface ISurveysService {

    /**
     * Retrieve the daily survey's data
     * @return The survey, if found. null otherwise
     */
    // TODO: Add thrown exceptions after implementation
    Questionnaire retrieveDailySurvey();

}
