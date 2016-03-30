package com.github.soniex2.notebetter.api;

import com.google.common.base.Objects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public class NoteBetterInstrument {
    @Nullable
    protected final ISoundEvent soundEvent;
    protected final float volume;

    @Deprecated
    public NoteBetterInstrument(@Nullable SoundEvent soundEvent, float volume) {
        this.soundEvent = new RegisteredSoundEvent(soundEvent);
        this.volume = volume;
    }

    public NoteBetterInstrument(@Nullable ISoundEvent soundEvent, float volume) {
        this.soundEvent = soundEvent;
        this.volume = volume;
    }

    public float volume() {
        return volume;
    }

    @Nullable
    @Deprecated
    public SoundEvent soundEvent() {
        return soundEvent == null ? null : SoundEvent.soundEventRegistry.getObject(soundEvent.asResourceLocation());
    }

    @Nullable
    public ISoundEvent iSoundEvent() {
        return soundEvent;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof NoteBetterInstrument)) return false;
        NoteBetterInstrument nbi = (NoteBetterInstrument) other;
        return volume == nbi.volume && Objects.equal(soundEvent, nbi.soundEvent);
    }

    @Override
    public int hashCode() {
        return ((soundEvent != null ? soundEvent.hashCode() * 31 : 0) + Float.floatToIntBits(volume)) * 31;
    }
}
