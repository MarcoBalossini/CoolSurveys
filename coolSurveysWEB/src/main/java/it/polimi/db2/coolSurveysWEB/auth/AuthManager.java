package it.polimi.db2.coolSurveysWEB.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.db2.coolSurveysWEB.auth.exceptions.TokenException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

/**
 * Class using JWT third-part library to generate tokens</br>
 * The class is a singleton to avoid excessive file reading from conf.json
 */
public class AuthManager implements IAuthManager {

    /**
     * ID field name in token
     */
    private static final String ID = "id";

    /**
     * Name of the key in conf.json
     */
    private static final String KEY_NAME = "secretCode";

    /**
     * Name of the issuer in conf.json
     */
    private static final String ISSUER_NAME = "issuer";

    /**
     * Encryption additional key
     */
    private String key;

    /**
     * Token issuer
     */
    private String issuer;

    /**
     * Used encryption algorithm
     */
    private final Algorithm algorithm;

    /**
     * JWT verifier
     */
    private final JWTVerifier verifier;

    /**
     * AuthManager instance
     */
    private static AuthManager authManagerInstance;

    /**
     * Getter for the active instance.</br>
     * If necessary a new instance is created
     * @return The active instance
     */
    public static AuthManager getInstance() {
        if (authManagerInstance == null)
            authManagerInstance = new AuthManager();

        return authManagerInstance;
    }

    /**
     * Class constructor. Initialize all attributes
     */
    private AuthManager() {
        getConfData();

        this.algorithm = Algorithm.HMAC512(key);

        verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int checkToken(String token) throws TokenException {
        int id;
        try {
            DecodedJWT jwt = verifier.verify(token);
            id = jwt.getClaim(ID).asInt();
        } catch (TokenExpiredException e) {
            throw new TokenException("Token Expired");
        } catch (JWTVerificationException e) {
            throw new TokenException();
        }
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateToken(int id, long validity) {
        if (validity <= 0)
            return null;

        String token = JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + validity))
                .withClaim(ID, id)
                .sign(algorithm);

        return token;
    }

    /**
     * Get configuration data from conf.json and initialize key and issuer
     */
    private void getConfData() {
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/conf.json"));
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(reader, JsonObject.class);
        JsonElement code = json.get(KEY_NAME);
        JsonElement issuer = json.get(ISSUER_NAME);
        if(code == null || issuer == null) {
            throw new RuntimeException("Configuration data not found");
        }
        this.key = code.getAsString();
        this.issuer = issuer.getAsString();
    }
}
