package com.github.soniex2.notebetter.note;

import com.github.soniex2.notebetter.NoteBetter;
import com.github.soniex2.notebetter.api.NoteBetterInstrument;
import com.github.soniex2.notebetter.config.util.JsonHelper;
import com.github.soniex2.notebetter.util.CachedResourceLocation;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author soniex2
 */
class NoteBetterInstruments {
    @Nullable
    protected NoteBetterInstrument defaultInstrument = null;
    protected Map<ResourceLocation, InstrumentBlock> blocks = new LinkedHashMap<ResourceLocation, InstrumentBlock>();
    protected List<MaterialSound> materials = new ArrayList<MaterialSound>();

    /**
     * Retrieve the default/fallback instrument.
     *
     * @return {@literal null} if there is no default instrument.
     */
    @Nullable
    public NoteBetterInstrument getDefaultInstrument() {
        return defaultInstrument;
    }

    /**
     * Retrieve the instrument assigned to a given material.
     *
     * @param material The material.
     * @return {@literal null} if the given material has no instrument assigned to it, or the assigned instrument otherwise.
     */
    @Nullable
    public NoteBetterInstrument getInstrumentForMaterial(@Nonnull Material material) {
        for (MaterialSound ms : materials) {
            ResourceLocation blockName = ms.getBlockName();
            // We already have a ResourceLocation, this is faster.
            Block b = null;
            if (Block.blockRegistry.containsKey(blockName)) {
                b = Block.blockRegistry.getObject(blockName);
            }
            if (b != null && b.getMaterial() == material) {
                return ms.getInstrument();
            }
        }
        return null;
    }

    /**
     * Retrieve the instrument assigned to a given block.
     *
     * @param block The block.
     * @return {@literal null} if the given block has no instrument assigned to it, or the assigned instrument otherwise.
     */
    @Nullable
    public NoteBetterInstrument getInstrumentForBlock(@Nonnull Block block, IBlockState state) {
        ResourceLocation rl = Block.blockRegistry.getNameForObject(block);
        if (rl != null && blocks.containsKey(rl)) { // null check is an optimization
            return blocks.get(rl).get(state);
        }
        return null;
    }

    /* JSON METHODS */

    private static final Gson JSON_ADAPTER = (new GsonBuilder())
            .registerTypeAdapter(NoteBetterInstruments.class, new NoteBetterInstruments.Serializer())
            .setPrettyPrinting()
            .create();

    public static NoteBetterInstruments fromString(String s) {
        if (s.length() == 0) {
            return new NoteBetterInstruments();
        }
        try {
            return JSON_ADAPTER.fromJson(s, NoteBetterInstruments.class);
        } catch (Exception exception) {
            return new NoteBetterInstruments();
        }
    }

    public String toString() {
        return JSON_ADAPTER.toJson(this);
    }

    /* JSON (DE)SERIALIZER */

    public static class Serializer implements JsonDeserializer<NoteBetterInstruments>, JsonSerializer<NoteBetterInstruments> {

        @Nonnull
        private NoteBetterInstrument getInstrument(@Nonnull JsonElement jsonElement) {
            @Nullable
            String name;
            float volume;
            if (jsonElement.isJsonPrimitive()) {
                name = jsonElement.getAsString();
                volume = 3f;
            } else if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement nameElement = jsonObject.get("name");
                if (nameElement == null) {
                    throw new JsonSyntaxException("Invalid instrument: " + jsonElement);
                }
                if (nameElement.isJsonPrimitive()) {
                    name = nameElement.getAsString();
                } else if (nameElement.isJsonNull()) {
                    name = null;
                } else {
                    throw new JsonSyntaxException("Invalid instrument: " + jsonElement);
                }
                volume = JsonUtils.getFloat(jsonObject, "volume", 3f);
            } else if (jsonElement.isJsonNull()) {
                name = null;
                volume = 3f;
            } else {
                throw new JsonSyntaxException("Invalid instrument: " + jsonElement);
            }
            return new NoteBetterInstrument(name != null ? new CachedResourceLocation(name) : null, volume);
        }

        /**
         * @inheritDoc
         */
        @Override
        public NoteBetterInstruments deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            NoteBetterInstruments config = new NoteBetterInstruments();
            try {
                JsonElement defaultSoundElement = jsonObject.get("default");
                if (defaultSoundElement != null && !defaultSoundElement.isJsonNull()) {
                    config.defaultInstrument = getInstrument(defaultSoundElement);
                }
                JsonArray blocks = null;//JsonHelper.optJsonArray(jsonObject, "blocks");
                JsonArray materials = null;//JsonHelper.optJsonArray(jsonObject, "materials");
                if (blocks != null) {
                    for (JsonElement element : blocks) {
                        JsonObject blockObject = JsonUtils.getJsonObject(element, "blocks");
                        String block = JsonUtils.getString(blockObject, "block");
                        JsonElement soundElement = blockObject.get("sound");
                        if (soundElement == null) throw new JsonSyntaxException("Invalid instrument");
                        InstrumentBlock is = new InstrumentBlock();
                        is.addPredicate(null, getInstrument(soundElement));// TODO read predicates
                        config.blocks.put(new CachedResourceLocation(block), is);
                    }
                }
                if (materials != null) {
                    for (JsonElement element : materials) {
                        JsonObject materialObject = JsonUtils.getJsonObject(element, "materials");
                        String block;
                        if (materialObject.has("material_of")) {
                            block = JsonUtils.getString(materialObject, "material_of");
                        } else {
                            block = JsonUtils.getString(materialObject, "material");
                        }
                        JsonElement soundElement = materialObject.get("sound");
                        if (soundElement == null) throw new JsonSyntaxException("Invalid instrument");
                        config.materials.add(new MaterialSound(new CachedResourceLocation(block), getInstrument(soundElement)));
                    }
                }
            } catch (Exception e) {
                NoteBetter.instance.log.error("Error decoding config file", e);
            }
            return config;
        }

        private JsonObject toSoundObject(NoteBetterInstrument instrument) {
            JsonObject jsonObject = new JsonObject();
            ResourceLocation soundEvent = instrument.getSoundEvent();
            if (soundEvent != null) {
                jsonObject.addProperty("name", soundEvent.toString());
            } else {
                jsonObject.add("name", JsonNull.INSTANCE);
            }
            jsonObject.addProperty("volume", instrument.getVolume());
            return jsonObject;
        }

        /**
         * @inheritDoc
         */
        @Override
        public JsonElement serialize(NoteBetterInstruments config, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            if (config.defaultInstrument != null) {
                jsonObject.add("default", toSoundObject(config.defaultInstrument));
            }
            JsonArray blocks = new JsonArray();
            JsonArray materials = new JsonArray();
            for (MaterialSound sound : config.materials) {
                JsonObject materialObject = new JsonObject();
                materialObject.addProperty("block", sound.getBlockName().toString());
                materialObject.add("sound", toSoundObject(sound.getInstrument()));
                materials.add(materialObject);
            }
            for (Map.Entry<ResourceLocation, InstrumentBlock> sound : config.blocks.entrySet()) {
                JsonObject blockObject = new JsonObject();
                blockObject.addProperty("block", sound.getKey().toString());
                //blockObject.add("sound", toSoundObject(sound.getValue())); // TODO
                blocks.add(blockObject);
            }
            jsonObject.add("materials", materials);
            jsonObject.add("blocks", blocks);
            return jsonObject;
        }
    }

}
