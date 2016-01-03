package com.github.soniex2.notebetter.config.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;

/**
 * @author soniex2
 */
public class JsonHelper {
    public static JsonArray getJsonArrayOrNull(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            if (jsonObject.isJsonNull()) {
                return null;
            }
            return JsonUtils.getJsonArray(jsonObject.get(key), key);
        } else {
            return null;
        }
    }
}
