package com.github.soniex2.notebetter.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class EventHelper {
    private static final String RESDOM = "minecraft";
    private static final String PREFIX = "block.note.";

    public static NoteBlockEvent.Instrument instrumentFromResLoc(ResourceLocation instrument) {
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
}
