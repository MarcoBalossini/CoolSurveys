package it.polimi.db2.coolSurveysWEB.auth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AuthManager implements IAuthManager {

    private static final String KEY_NAME = "secretCode";

    private String key;

    private static AuthManager authManagerInstance;

    public static AuthManager getInstance() {
        if (authManagerInstance == null)
            authManagerInstance = new AuthManager();

        return authManagerInstance;
    }

    private AuthManager() {
        getSecretKey();
    }

    @Override
    public boolean checkToken(String token) {
        String usrn = token.split(":")[0];
        return generateToken(usrn).equals(token);
    }

    @Override
    public String generateToken(String usrn) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String toHash = usrn + key;
            return usrn + ":" + Arrays.toString(md.digest(toHash.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void getSecretKey() {
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/conf.json"));
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(reader, JsonObject.class);
        JsonElement code = json.get(KEY_NAME);
        if(code == null) {
            throw new RuntimeException("Code not found");
        }
        key = code.getAsString();
    }
}
