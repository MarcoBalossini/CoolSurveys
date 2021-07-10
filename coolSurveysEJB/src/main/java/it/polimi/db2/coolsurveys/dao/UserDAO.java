package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.BadWord;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.Log;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class UserDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    public UserDAO(EntityManager em) {
        this.em = em;
    }

    public UserDAO() {}

    public User insertUser(String username, String password, String mail, boolean admin) throws AlreadyExistsException {

        if (username == null || password == null || mail == null)
            throw new IllegalArgumentException();

        if(em.createNamedQuery("User.selectByUsername")
                .setParameter("username", username).getResultList().size() > 0)
            throw new AlreadyExistsException("User already exists");

        User newUser = new User();
        Credentials newCredentials = new Credentials();
        newUser.setCredentials(newCredentials);
        newCredentials.setUsername(username);
        newCredentials.setAdmin(admin);

        newCredentials.setPassword_hash(password);
        newCredentials.setMail(mail);

        em.persist(newCredentials);
        em.persist(newUser);


        return newUser;
    }

    public User retrieveUserByUsername(String username) throws NotFoundException {

        User user;

        try {
            user = em.createNamedQuery("User.selectByUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public User retrieveUserByMail (String mail) throws NotFoundException {

        User user;

        try {
            user = em.createNamedQuery("User.selectByMail", User.class)
                    .setParameter("mail", mail)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    public List<User> getLeaderBoard() {
        List<User> list = em.createNamedQuery("User.selectDailyLeaderboard", User.class)
                .setParameter("date", LocalDate.now()).getResultList();
        for(User u: list)
            em.refresh(u);

        return list;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void banUser(User user) {
        user.setBlockedUntil(LocalDateTime.now().plusMonths(BadWord.MONTHS_TO_BAN));
        em.merge(user);


    }

    public User find(int id) throws NotFoundException {
        User user = em.find(User.class, id);

        if(user != null)
            return user;

        throw new NotFoundException("User not found");
    }

    public void log(User user) {
        Log log = new Log(user);
        em.persist(log);
    }

}
