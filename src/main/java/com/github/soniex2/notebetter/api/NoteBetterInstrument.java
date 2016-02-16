package com.github.soniex2.notebetter.api;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public class NoteBetterInstrument {
    @Nullable
    protected final ResourceLocation soundEvent;
    protected final float volume;

    public NoteBetterInstrument(@Nullable ResourceLocation soundEvent, float volume) {
        this.soundEvent = soundEvent;
        this.volume = volume;
    }

    public float getVolume() {
        return volume;
    }

    @Nullable
    public ResourceLocation getSoundEvent() {
        return soundEvent;
    }
}
