package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.PersistenceTest;
import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.dao.exceptions.NotFoundException;
import it.polimi.db2.coolsurveys.entities.BadWord;
import it.polimi.db2.coolsurveys.entities.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest extends PersistenceTest {

    @Test
    public void insertUser() {
        em.getTransaction().begin();

        UserDAO dao = new UserDAO(em);
        try {
            User u = dao.insertUser("user1", "user1password", "user1@user.com", false);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
            fail();
        }
        em.getTransaction().commit();

    }

    @Test
    public void banUser() {

        insertUser();

        em.getTransaction().begin();

        UserDAO dao = new UserDAO(em);

        try {
            User u = dao.retrieveUserByUsername("user1");
            LocalDateTime blockedUntil = u.getBlockedUntil();

            dao.banUser(u);

            em.getTransaction().commit();

            assert(u.getBlockedUntil().isAfter(blockedUntil.plusMonths(BadWord.MONTHS_TO_BAN)));

        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void logUser() {
        em.getTransaction().begin();

        UserDAO dao = new UserDAO(em);
        try {
            User u = dao.insertUser("user1", "user1password", "user1@user.com", false);
            dao.log(u);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
            fail();
        }
        em.getTransaction().commit();
    }

    @AfterEach
    @BeforeEach
    public void clean() {
        em.getTransaction().begin();
        em.createNativeQuery("delete from user").executeUpdate();
        em.createNativeQuery("delete from credentials").executeUpdate();
        em.getTransaction().commit();

    }

}
