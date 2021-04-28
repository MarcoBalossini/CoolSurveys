package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.Credentials;
import it.polimi.db2.coolsurveys.entities.User;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class UserDAO {
    @PersistenceContext(unitName = "coolSurveys")
    private EntityManager em;

    private static final int monthsToBeBanned = 1;

    protected UserDAO(EntityManager em) {
        this.em = em;
    }

    public UserDAO() {}

    public User insertUser(String username, String password, String mail) throws AlreadyExistsException {


        System.out.println("Adding user " + username);

        if (username == null || password == null || mail == null)
            throw new IllegalArgumentException();

        if(em.createNamedQuery("User.selectByUsername")
                .setParameter("username", username).getResultList().size() > 0)
            throw new AlreadyExistsException("User already exists");

        System.out.println("Persisting user " + username);

        User newUser = new User();
        Credentials newCredentials = new Credentials();
        newUser.setCredentials(newCredentials);
        newCredentials.setUsername(username);

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
            throw new NotFoundException();
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
            throw new NotFoundException();
        }

        return user;
    }

    public User banUser(User user) {
        user.setBlockedUntil(user.getBlockedUntil().plusMonths(monthsToBeBanned));
        em.merge(user);

        return user;
    }

}
