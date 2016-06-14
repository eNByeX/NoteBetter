package com.github.soniex2.notebetter.api.soundevent;

import com.google.common.base.Objects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class RegisteredSoundEvent implements ISoundEvent {
    private final SoundEvent soundEvent;

    public RegisteredSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    @Override
    public void play(World world, BlockPos blockPos, SoundCategory soundCategory, float volume, int note) {
        float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
        world.playSound(null, blockPos, soundEvent, SoundCategory.RECORDS, volume, pitch);
    }

    @Override
    public ResourceLocation asResourceLocation() {
        return soundEvent.getSoundName();
    }

    private static final String RESDOM = "minecraft";
    private static final String PREFIX = "block.note.";

    @Override
    public NoteBlockEvent.Instrument asVanillaInstrument() {
        ResourceLocation instrument = asResourceLocation();
        if (instrument.getResourceDomain().equals(RESDOM) && instrument.getResourcePath().startsWith(PREFIX)) {
            String name = instrument.getResourcePath().substring(PREFIX.length()); // skip above prefix
            switch (name.charAt(0)) {
                case 'h':
                    if (name.equals("harp"))
                        return NoteBlockEvent.Instrument.PIANO;
                    else if (name.equals("hat"))
                        return NoteBlockEvent.Instrument.CLICKS;
                    break;
                case 'b':
                    if (name.equals("basedrum"))
                        return NoteBlockEvent.Instrument.BASSDRUM;
                    else if (name.equals("bass"))
                        return NoteBlockEvent.Instrument.BASSGUITAR;
                    break;
                case 's':
                    if (name.equals("snare"))
                        return NoteBlockEvent.Instrument.SNARE;
                    break;
            }
        }
        return NoteBlockEvent.Instrument.PIANO;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RegisteredSoundEvent)) return false;
        RegisteredSoundEvent nbi = (RegisteredSoundEvent) other;
        return Objects.equal(nbi.soundEvent, soundEvent);
    }

    @Override
    public int hashCode() {
        return asResourceLocation().hashCode();
    }

    @Override
    public String toString() {
        return asResourceLocation().toString();
    }
}
