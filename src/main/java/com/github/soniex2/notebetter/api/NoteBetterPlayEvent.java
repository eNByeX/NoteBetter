package com.github.soniex2.notebetter.api;

import com.github.soniex2.notebetter.api.soundevent.RegisteredSoundEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class NoteBetterPlayEvent extends NoteBlockEvent.Play {

    private NoteBetterInstrument noteBetterInstrument;

    public NoteBetterPlayEvent(World world, BlockPos pos, IBlockState state, int note, NoteBetterInstrument noteBetterInstrument) {
        super(world, pos, state, note, noteBetterInstrument.asVanillaInstrument().ordinal());
        this.noteBetterInstrument = noteBetterInstrument;
    }

    public NoteBetterInstrument noteBetterInstrument() {
        return noteBetterInstrument;
    }

    public void noteBetterInstrument(NoteBetterInstrument instrument) {
        this.noteBetterInstrument = instrument;
        super.setInstrument(instrument.asVanillaInstrument());
    }

    private static NoteBetterInstrument instrumentToNB(NoteBlockEvent.Instrument instrument) {
        switch (instrument) {
            default:
                return new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_HARP), 3f);
            case BASSDRUM:
                return new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_BASEDRUM), 3f);
            case SNARE:
                return new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_SNARE), 3f);
            case CLICKS:
                return new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_HAT), 3f);
            case BASSGUITAR:
                return new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_BASS), 3f);
        }
    }

    public void setInstrument(Instrument instrument) {
        super.setInstrument(instrument);
        this.noteBetterInstrument = instrumentToNB(instrument);
    }
}
