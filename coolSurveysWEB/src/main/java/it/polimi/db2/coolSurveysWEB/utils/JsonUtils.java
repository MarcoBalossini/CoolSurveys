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
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtils {

    private static JsonUtils jsonUtilsInstance;

    private List<JsonObject> permanentQuestions;

    public static JsonUtils getInstance() {
        if (jsonUtilsInstance == null)
            jsonUtilsInstance = new JsonUtils();
        return jsonUtilsInstance;
    }

    private JsonUtils() {}

    public static String getJson(HttpServletRequest request) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);

        return jb.toString();
    }

    /**
     * Get json body from request
     * @param request The request
     * @return The json string in the body
     * @throws IOException When a reader error occurs
     */
    public static JsonObject getJsonFromRequest(HttpServletRequest request) throws IOException {
        return new Gson().fromJson(getJson(request), JsonElement.class).getAsJsonObject();
    }

    public List<JsonObject> getPermanentQuestions() {
        if (permanentQuestions == null)
            readPermanentQuestions();
        return permanentQuestions;
    }

    /**
     * Reads permanent questions from permanentQuestions.json
     */
    private void readPermanentQuestions() {
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/permanentQuestions.json"));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

        List<JsonObject> jsonQuestions = new ArrayList<>();
        jsonArray.forEach(question -> jsonQuestions.add(question.getAsJsonObject()));

        permanentQuestions = jsonQuestions;
    }

    public static Map<String, String> convertToMap(JsonObject jsonObject) {
        return jsonObject.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getAsString()));
    }

    /**
     * Convert a JsonObject Map into a Java Map(String, List(String))
     * @param jsonObject The JSON map
     * @return The Java Map
     */
    public static Map<String, List<String>> convertToMapStringList(JsonObject jsonObject) {
        return jsonObject.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            List<String> options = new ArrayList<>();
            entry.getValue().getAsJsonArray().forEach(option -> options.add(((JsonObject) option).get("option").getAsString()));

            return options;
        }));
    }

}
