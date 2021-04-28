package it.polimi.db2.coolsurveys.services;

import it.polimi.db2.coolsurveys.dao.QuestionnaireDAO;
import it.polimi.db2.coolsurveys.entities.Option;
import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.Questionnaire;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class SurveysService implements ISurveysService {

    @EJB(name = "it.polimi.db2.coolsurveys.dao/UserDAO")
    protected QuestionnaireDAO dataAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public Questionnaire retrieveDailySurvey() {
        return new Questionnaire();
    }
}
