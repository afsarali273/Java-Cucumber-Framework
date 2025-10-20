package com.automation.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public static String toJson(Object object) {
        return gson.toJson(object);
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
    
    public static JsonObject parseJson(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
    
    public static String prettyPrint(String json) {
        JsonObject jsonObject = parseJson(json);
        return gson.toJson(jsonObject);
    }
}
