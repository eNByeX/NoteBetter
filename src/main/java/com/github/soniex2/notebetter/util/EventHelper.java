package com.github.soniex2.notebetter.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class EventHelper {
    public static NoteBlockEvent.Instrument instrumentFromResLoc(ResourceLocation instrument) {
        if (instrument.getResourceDomain().equals("minecraft") && instrument.getResourcePath().startsWith("note.")) {
            String name = instrument.getResourcePath().substring(5); // skip above prefix
            switch (name.charAt(0)) {
                case 'h':
                    if (name.equals("harp"))
                        return NoteBlockEvent.Instrument.PIANO;
                    else if (name.equals("hat"))
                        return NoteBlockEvent.Instrument.CLICKS;
                    break;
                case 'b':
                    if (name.equals("bd"))
                        return NoteBlockEvent.Instrument.BASSDRUM;
                    else if (name.equals("bassattack"))
                        return NoteBlockEvent.Instrument.BASSGUITAR;
                    break;
                case 's':
                    if (name.equals("snare"))
                        return NoteBlockEvent.Instrument.SNARE;
                    break;
            }
        }
        return null;
    }

    private static final ResourceLocation RL_PIANO = new CachedResourceLocation("minecraft:note.harp");
    private static final ResourceLocation RL_BASS_DRUM = new CachedResourceLocation("minecraft:note.bd");
    private static final ResourceLocation RL_SNARE = new CachedResourceLocation("minecraft:note.snare");
    private static final ResourceLocation RL_CLICKS = new CachedResourceLocation("minecraft:note.hat");
    private static final ResourceLocation RL_DOUBLE_BASS = new CachedResourceLocation("minecraft:note.bassattack");

    public static ResourceLocation instrumentToResLoc(NoteBlockEvent.Instrument instrument) {
        switch (instrument) {
            default:
                return RL_PIANO;
            case BASSDRUM:
                return RL_BASS_DRUM;
            case SNARE:
                return RL_SNARE;
            case CLICKS:
                return RL_CLICKS;
            case BASSGUITAR:
                return RL_DOUBLE_BASS;
        }
    }
}
