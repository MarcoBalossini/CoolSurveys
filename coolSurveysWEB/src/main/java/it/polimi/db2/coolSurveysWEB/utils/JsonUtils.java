package it.polimi.db2.coolSurveysWEB.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static JsonUtils jsonUtilsInstance;

    private List<JsonObject> permanentQuestions;

    public static JsonUtils getInstance() {
        if (jsonUtilsInstance == null)
            jsonUtilsInstance = new JsonUtils();
        return jsonUtilsInstance;
    }

    private JsonUtils() {}

    public static JsonObject getJsonFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);

        return new Gson().fromJson(jb.toString(), JsonElement.class).getAsJsonObject();
    }

    public List<JsonObject> getPermanentQuestions() {
        if (permanentQuestions == null)
            readPermanentQuestions();
        return permanentQuestions;
    }

    private void readPermanentQuestions() {
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/permanentQuestions.json"));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

        List<JsonObject> jsonQuestions = new ArrayList<>();
        jsonArray.forEach((question) -> jsonQuestions.add(question.getAsJsonObject()));

        permanentQuestions = jsonQuestions;
    }

}
