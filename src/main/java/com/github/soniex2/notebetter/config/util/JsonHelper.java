package com.github.soniex2.notebetter.config.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * @author soniex2
 */
public class JsonHelper {
    public static String getStringOrNull(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            JsonElement value = jsonObject.get(key);
            if (value.isJsonPrimitive())
                return value.getAsString();
            else if (value.isJsonNull())
                return null;
            throw new JsonSyntaxException(key + " must be string or null!");
        }
        return null;
    }
}
