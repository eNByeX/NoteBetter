package com.github.soniex2.notebetter.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public class NoteBetterInstrument {
    @Nullable
    protected final SoundEvent soundEvent;
    protected final float volume;

    public NoteBetterInstrument(@Nullable SoundEvent soundEvent, float volume) {
        this.soundEvent = soundEvent;
        this.volume = volume;
    }

    public float volume() {
        return volume;
    }

    @Nullable
    public SoundEvent soundEvent() {
        return soundEvent;
    }
}
