package it.polimi.db2.coolsurveys;

import it.polimi.db2.coolsurveys.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DataAccess implements DataAccessInt{

    @PersistenceContext(unitName = "coolSurveys")
    protected EntityManager em;

    /**
     * Retrieves a user from the database, given its username as parameter
     * @return registered user
     */
    @Override
    public User retrieveUserByUsername(String username) {

        User user;

        try {
            user = em.createNamedQuery("User.selectByUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }
        return user;
    }

    @Override
    public User retrieveUserByMail (String mail) {

        User user;

        try {
            user = em.createNamedQuery("User.selectByMail", User.class)
                    .setParameter("mail", mail)
                    .getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }

        return user;
    }

    @Override
    public boolean insertUser(String username, String password, String mail) {

        boolean result = true;

        System.out.println("Adding user " + username);

        if (username == null || password == null)
            return false;

        if(em.createNamedQuery("User.selectByUsername")
                .setParameter("username", username).getResultList().size() > 0)
            result = false;

        else {

            System.out.println("Persisting user " + username);

            User newUser = new User();
            Credentials newCredentials = new Credentials();
            newCredentials.setUsername(username);
            //TODO: hash
            newCredentials.setPassword_hash(password);
            newCredentials.setMail(mail);
            newUser.setUser_id(newCredentials.getUser_id());

            em.persist(newCredentials);
            em.persist(newUser);
        }

        return result;
    }

    @Override
    public boolean insertQuestionnaire (String name, byte[] photo, List<Question> questionList) {
        boolean result = true;

        if (em.createNamedQuery("Questionnaire.selectByName")
                .setParameter("name", name).getResultList().size() > 0)
            result = false;

        else {
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setName(name);
            questionnaire.setQuestions(questionList);
            questionnaire.setPhoto(photo);

            em.persist(questionnaire);
        }

        return result;
    }

    @Override
    public boolean insertQuestion(String question, String questionnaire_name, List<String> options) {
        boolean result = true;

        if (em.createNamedQuery("Question.selectByQuestion")
                .setParameter("question", question).getResultList().size() > 0)
            result = false;
        else {
            Question newQuestion = new Question();
            newQuestion.setQuestion(question);
            Questionnaire questionnaire = (Questionnaire) em.createNamedQuery("Questionnaire.selectByName")
                    .setParameter("name", questionnaire_name).getSingleResult();
            newQuestion.setQuestionnaire(questionnaire);

            newQuestion.setQuestionnaire_id(questionnaire.getQ_id());

            List<Option> optionsFromStrings = new ArrayList<>();
            Option newOption;
            for (String option: options) {

                newOption = new Option();
                newOption.setQuestion_id(newQuestion.getQuestion_id());
                newOption.setQuestionnaire(questionnaire);
                newOption.setQuestionnaire_id(questionnaire.getQ_id());
                newOption.setText(option);

                em.persist(newOption);

                optionsFromStrings.add(newOption);
            }

            newQuestion.setOptions(optionsFromStrings);

            em.persist(question);
        }

        return result;
    }
}
