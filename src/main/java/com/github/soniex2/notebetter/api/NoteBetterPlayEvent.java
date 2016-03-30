package com.github.soniex2.notebetter.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class NoteBetterPlayEvent extends NoteBlockEvent.Play {

    private static class EventHelper {
        private static final String RESDOM = "minecraft";
        private static final String PREFIX = "block.note.";

        public static NoteBlockEvent.Instrument instrumentFromSoundEvent(ISoundEvent soundEvent) {
            ResourceLocation instrument = soundEvent.asResourceLocation();
            if (instrument.getResourceDomain().equals(RESDOM) && instrument.getResourcePath().startsWith(PREFIX)) {
                String name = instrument.getResourcePath().substring(PREFIX.length()); // skip above prefix
                switch (name.charAt(0)) {
                    case 'h':
                        if (name.equals("harp"))
                            return Instrument.PIANO;
                        else if (name.equals("hat"))
                            return Instrument.CLICKS;
                        break;
                    case 'b':
                        if (name.equals("basedrum"))
                            return Instrument.BASSDRUM;
                        else if (name.equals("bass"))
                            return Instrument.BASSGUITAR;
                        break;
                    case 's':
                        if (name.equals("snare"))
                            return Instrument.SNARE;
                        break;
                }
            }
            return Instrument.PIANO;
        }

        private static final NoteBetterInstrument NB_PIANO = new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.block_note_harp), 3f);
        private static final NoteBetterInstrument NB_BASS_DRUM = new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.block_note_basedrum), 3f);
        private static final NoteBetterInstrument NB_SNARE = new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.block_note_snare), 3f);
        private static final NoteBetterInstrument NB_CLICKS = new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.block_note_hat), 3f);
        private static final NoteBetterInstrument NB_DOUBLE_BASS = new NoteBetterInstrument(new RegisteredSoundEvent(SoundEvents.block_note_bass), 3f);

        public static NoteBetterInstrument instrumentToNB(NoteBlockEvent.Instrument instrument) {
            switch (instrument) {
                default:
                    return NB_PIANO;
                case BASSDRUM:
                    return NB_BASS_DRUM;
                case SNARE:
                    return NB_SNARE;
                case CLICKS:
                    return NB_CLICKS;
                case BASSGUITAR:
                    return NB_DOUBLE_BASS;
            }
        }
    }

    private NoteBetterInstrument noteBetterInstrument;

    public NoteBetterPlayEvent(World world, BlockPos pos, IBlockState state, int note, NoteBetterInstrument instrument) {
        super(world, pos, state, note, EventHelper.instrumentFromSoundEvent(instrument.iSoundEvent()).ordinal());
        this.noteBetterInstrument = instrument;
    }

    public NoteBetterInstrument noteBetterInstrument() {
        return noteBetterInstrument;
    }

    public void noteBetterInstrument(NoteBetterInstrument instrument) {
        this.noteBetterInstrument = instrument;
        super.setInstrument(EventHelper.instrumentFromSoundEvent(instrument.iSoundEvent()));
    }

    public void setInstrument(Instrument instrument) {
        super.setInstrument(instrument);
        this.noteBetterInstrument = EventHelper.instrumentToNB(instrument);
    }
}
