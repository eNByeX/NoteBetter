package com.github.soniex2.notebetter.config;

import com.github.soniex2.notebetter.config.util.JsonHelper;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author soniex2
 */
public class NoteBetterNoteConfig {
    private static final Gson JSON_ADAPTER = (new GsonBuilder())
            .registerTypeAdapter(NoteBetterNoteConfig.class, new NoteBetterNoteConfig.Serializer())
            .setPrettyPrinting()
            .create();
    public String base = null;
    public Map<String, String> blocks = new LinkedHashMap<String, String>();
    public List<MaterialSound> materials = new ArrayList<MaterialSound>();

    public static NoteBetterNoteConfig fromString(String s) {
        if (s.length() == 0) {
            return new NoteBetterNoteConfig();
        } else {
            try {
                return JSON_ADAPTER.fromJson(s, NoteBetterNoteConfig.class);
            } catch (Exception exception) {
                return new NoteBetterNoteConfig();
            }
        }
    }

    public String toString() {
        return JSON_ADAPTER.toJson(this);
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer {

        /**
         * @inheritDoc
         */
        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            NoteBetterNoteConfig config = new NoteBetterNoteConfig();
            try {
                config.base = JsonHelper.getStringOrNull(jsonObject, "default");
                JsonArray blocks = jsonObject.has("blocks") ? JsonUtils.getJsonElementAsJsonArray(jsonObject.get("blocks"), "blocks") : null;
                JsonArray materials = jsonObject.has("materials") ? JsonUtils.getJsonElementAsJsonArray(jsonObject.get("materials"), "materials") : null;
                if (blocks != null) {
                    for (JsonElement element : blocks) {
                        JsonObject materialObject = JsonUtils.getElementAsJsonObject(element, "blocks");
                        String block = JsonUtils.getJsonObjectStringFieldValue(materialObject, "block");
                        String sound = JsonHelper.getStringOrNull(materialObject, "sound");
                        config.blocks.put(block, sound);
                    }
                }
                if (materials != null) {
                    for (JsonElement element : materials) {
                        JsonObject materialObject = JsonUtils.getElementAsJsonObject(element, "materials");
                        String material_of = JsonUtils.getJsonObjectStringFieldValue(materialObject, "material_of");
                        String sound = JsonHelper.getStringOrNull(materialObject, "sound");
                        config.materials.add(new MaterialSound(material_of, sound));
                    }
                } else {
                    config.materials.add(new MaterialSound("minecraft:stone", "minecraft:note.bd"));
                    config.materials.add(new MaterialSound("minecraft:sand", "minecraft:note.snare"));
                    config.materials.add(new MaterialSound("minecraft:glass", "minecraft:note.hat"));
                    config.materials.add(new MaterialSound("minecraft:planks", "minecraft:note.bassattack"));
                }
            } catch (Exception ignore) {
            }
            return config;
        }

        /**
         * @inheritDoc
         */
        @Override
        public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
            NoteBetterNoteConfig config = (NoteBetterNoteConfig) src;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("default", config.base);
            JsonArray blocks = new JsonArray();
            JsonArray materials = new JsonArray();
            for (MaterialSound sound : config.materials) {
                JsonObject materialObject = new JsonObject();
                materialObject.addProperty("material_of", sound.material_of);
                materialObject.addProperty("sound", sound.sound);
                materials.add(materialObject);
            }
            for (Map.Entry<String, String> sound : config.blocks.entrySet()) {
                JsonObject blockObject = new JsonObject();
                blockObject.addProperty("material_of", sound.getKey());
                blockObject.addProperty("sound", sound.getValue());
                blocks.add(blockObject);
            }
            jsonObject.add("materials", materials);
            jsonObject.add("blocks", blocks);
            return jsonObject;
        }
    }

    public static class MaterialSound {
        public final String material_of;
        public final String sound;

        public MaterialSound(String material_of, String sound) {
            this.material_of = material_of;
            this.sound = sound;
        }
    }
}
