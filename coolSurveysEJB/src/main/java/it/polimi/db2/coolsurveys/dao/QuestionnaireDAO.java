package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class QuestionnaireDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public QuestionnaireDAO(EntityManager em) {
        this.em = em;
    }

    public QuestionnaireDAO() {}

    public Questionnaire insertQuestionnaire (LocalDate date, String name, byte[] photo, List<Question> questionList) throws AlreadyExistsException {

        if(name == null || photo == null || questionList == null || name.isEmpty() || questionList.isEmpty() || photo.length == 0)
            throw new IllegalArgumentException();

        if (em.createNamedQuery("Questionnaire.selectByName")
                .setParameter("name", name).getResultList().size() > 0)
            throw new AlreadyExistsException("Questionnaire already exists");

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDate(date);
        questionnaire.setName(name);
        questionnaire.setPhoto(photo);

        for(Question q : questionList) {
            q.setQuestionnaire(questionnaire);
            questionnaire.addQuestion(q);
        }

        em.persist(questionnaire);

        for(Question q: questionnaire.getQuestions())
            em.persist(q);

        return questionnaire;
    }

    public Questionnaire getByName(String name) throws NotFoundException {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();

        try {
            return em.createNamedQuery("Questionnaire.selectByName", Questionnaire.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Questionnaire \"" + name + "\" not found");
        }
    }

    public Questionnaire getByDate(LocalDate date) throws NotFoundException {
        if(date == null)
            throw new IllegalArgumentException();

        try {
            return em.createNamedQuery("Questionnaire.selectByDate", Questionnaire.class).setParameter("date", date).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("No Questionnaire defined for the date " + date.toString());
        }
    }

    public List<Questionnaire> findAll() {
        return em.createNamedQuery("Questionnaire.findAll", Questionnaire.class).getResultList();
    }

    public void removeQuestionnaire(Questionnaire questionnaire) {
        em.remove(questionnaire);
    }

    public List<String> getReviews() throws NotFoundException {
        return getByDate(LocalDate.now()).getReviews().stream().map(Review::getReview).collect(Collectors.toList());
    }

}
