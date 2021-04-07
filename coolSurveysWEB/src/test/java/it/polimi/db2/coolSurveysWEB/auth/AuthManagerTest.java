package it.polimi.db2.coolSurveysWEB.auth;

import it.polimi.db2.coolSurveysWEB.auth.exceptions.TokenException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class AuthManagerTest {

    private static AuthManager authManager;

    @BeforeAll
    public static void loadAuthManager() {
        authManager = AuthManager.getInstance();
    }

    @Test
    public void testTokenValidity() throws TokenException, InterruptedException {
        //Test wrong token validity
        assertEquals(null, authManager.generateToken(1, 0));

        //Verify token invalidity
        String token = authManager.generateToken(1, 1);
        TimeUnit.SECONDS.sleep(1);
        assertThrows(TokenException.class, () -> authManager.checkToken(token));

        //Verify token validity
        String token2 = authManager.generateToken(1, 1000);
        assertDoesNotThrow(() -> authManager.checkToken(token2));
    }

}