package it.polimi.db2.coolsurveys;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

abstract public class PersistenceTest {
    static protected EntityManagerFactory emf;
    static protected EntityManager em;

    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("coolSurveysTest");
        em = emf.createEntityManager();
    }

    @AfterAll
    public static void close() {
        em.close();
        emf.close();
    }

}
