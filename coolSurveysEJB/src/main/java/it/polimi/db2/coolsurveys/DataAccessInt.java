package it.polimi.db2.coolsurveys;

import it.polimi.db2.coolsurveys.entities.Question;
import it.polimi.db2.coolsurveys.entities.User;

import java.util.List;

public interface DataAccessInt {

    User retrieveUserByUsername(String username);
    User retrieveUserByMail(String mail);
    boolean insertUser(String username, String password, String mail);
    boolean insertQuestionnaire (String name, byte[] photo, List<Question> questionList);
    boolean insertQuestion (String question, String questionnaire_name, List<String> options);
}
