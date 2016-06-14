package com.github.soniex2.notebetter.api;

import com.github.soniex2.notebetter.api.soundevent.ISoundEvent;
import com.google.common.base.Objects;
import net.minecraftforge.event.world.NoteBlockEvent;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public class NoteBetterInstrument {
    @Nullable
    protected final ISoundEvent soundEvent;
    protected final float volume;

    public NoteBetterInstrument(@Nullable ISoundEvent soundEvent, float volume) {
        this.soundEvent = soundEvent;
        this.volume = volume;
    }

    public float volume() {
        return volume;
    }

    @Nullable
    public ISoundEvent soundEvent() {
        return soundEvent;
    }

    public NoteBlockEvent.Instrument asVanillaInstrument() {
        ISoundEvent se = soundEvent();
        if (se != null) {
            return se.asVanillaInstrument();
        } else {
            return NoteBlockEvent.Instrument.PIANO;
        }
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
