package it.polimi.db2.coolsurveys.dao;

import it.polimi.db2.coolsurveys.dao.exceptions.AlreadyExistsException;
import it.polimi.db2.coolsurveys.entities.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest extends DAOTest{

    @Test
    public void insertUser() {
        em.getTransaction().begin();

        UserDAO dao = new UserDAO(em);
        try {
            User u = dao.insertUser("user1", "user1password", "user1@user.com");
        } catch (AlreadyExistsException e) {
            fail();
        }
        em.getTransaction().commit();

    }

    @Override
    @AfterEach
    @BeforeEach
    public void clean() {
        em.getTransaction().begin();
        em.createNativeQuery("delete from user").executeUpdate();
        em.createNativeQuery("delete from credentials").executeUpdate();
        em.getTransaction().commit();

    }

}
