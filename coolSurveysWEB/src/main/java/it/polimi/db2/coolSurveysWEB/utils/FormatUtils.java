package it.polimi.db2.coolSurveysWEB.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class FormatUtils {

    public static JsonObject getJSON(HttpServletRequest request) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);

        return new Gson().fromJson(jb.toString(), JsonElement.class).getAsJsonObject();
    }

}
