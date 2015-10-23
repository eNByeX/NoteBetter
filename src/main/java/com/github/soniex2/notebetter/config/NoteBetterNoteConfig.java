package com.github.soniex2.notebetter.config;

import com.github.soniex2.notebetter.config.util.JsonHelper;
import com.github.soniex2.notebetter.util.CachedResourceLocation;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

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
    public ResourceLocation base = null;
    public Map<ResourceLocation, ResourceLocation> blocks = new LinkedHashMap<ResourceLocation, ResourceLocation>();
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
                String baseStr = JsonHelper.getStringOrNull(jsonObject, "default");
                config.base = baseStr != null ? new CachedResourceLocation(baseStr) : null;
                JsonArray blocks = jsonObject.has("blocks") ? JsonUtils.getJsonElementAsJsonArray(jsonObject.get("blocks"), "blocks") : null;
                JsonArray materials = jsonObject.has("materials") ? JsonUtils.getJsonElementAsJsonArray(jsonObject.get("materials"), "materials") : null;
                if (blocks != null) {
                    for (JsonElement element : blocks) {
                        JsonObject materialObject = JsonUtils.getElementAsJsonObject(element, "blocks");
                        String block = JsonUtils.getJsonObjectStringFieldValue(materialObject, "block");
                        String sound = JsonHelper.getStringOrNull(materialObject, "sound");
                        config.blocks.put(new CachedResourceLocation(block), sound != null ? new CachedResourceLocation(sound) : null);
                    }
                }
                if (materials != null) {
                    for (JsonElement element : materials) {
                        JsonObject materialObject = JsonUtils.getElementAsJsonObject(element, "materials");
                        String material_of = JsonUtils.getJsonObjectStringFieldValue(materialObject, "material_of");
                        String sound = JsonHelper.getStringOrNull(materialObject, "sound");
                        config.materials.add(new MaterialSound(new CachedResourceLocation(material_of), sound != null ? new CachedResourceLocation(sound) : null));
                    }
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
            if (config.base != null)
                jsonObject.addProperty("default", config.base.toString());
            else
                jsonObject.add("default", JsonNull.INSTANCE);
            JsonArray blocks = new JsonArray();
            JsonArray materials = new JsonArray();
            for (MaterialSound sound : config.materials) {
                JsonObject materialObject = new JsonObject();
                materialObject.addProperty("material_of", sound.material_of.toString());
                if (sound.sound != null)
                    materialObject.addProperty("sound", sound.sound.toString());
                else
                    materialObject.add("sound", JsonNull.INSTANCE);
                materials.add(materialObject);
            }
            for (Map.Entry<ResourceLocation, ResourceLocation> sound : config.blocks.entrySet()) {
                JsonObject blockObject = new JsonObject();
                blockObject.addProperty("material_of", sound.getKey().toString());
                if (sound.getValue() != null)
                    blockObject.addProperty("sound", sound.getValue().toString());
                else
                    blockObject.add("sound", JsonNull.INSTANCE);
                blocks.add(blockObject);
            }
            jsonObject.add("materials", materials);
            jsonObject.add("blocks", blocks);
            return jsonObject;
        }
    }

    public static class MaterialSound {
        public final ResourceLocation material_of;
        public final ResourceLocation sound;

        public MaterialSound(ResourceLocation material_of, ResourceLocation sound) {
            this.material_of = material_of;
            this.sound = sound;
        }
    }
}
