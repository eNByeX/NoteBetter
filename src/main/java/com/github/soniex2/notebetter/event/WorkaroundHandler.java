package com.github.soniex2.notebetter.event;

import com.github.soniex2.notebetter.util.EventHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author soniex2
 */
public class WorkaroundHandler {
    public static class NoteTickWorkaround {
        private final BlockPos pos;
        private int note;
        private final float volume;
        private ResourceLocation instrument;

        public NoteTickWorkaround(BlockPos pos, float volume, int note, ResourceLocation instrument) {
            this.pos = pos;
            this.instrument = instrument;
            this.volume = volume;
            this.note = note;
        }

        @Override
        public int hashCode() {
            return (((note * 31 + Float.floatToIntBits(volume)) * 31 + instrument.hashCode()) * 31 + pos.hashCode()) * 31;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof NoteTickWorkaround)) return false;
            NoteTickWorkaround ntw = (NoteTickWorkaround) other;
            /* optimization, saves storage space */
            return ntw.pos.equals(pos) ? ntw.note == note ? ntw.volume == volume ? ntw.instrument.equals(instrument) : false : false : false;
        }

        public void play(World world) {
            // Our event
            /*NoteBetterEvent.Play event = new NoteBetterEvent.Play(world, pos, world.getBlockState(pos), note, instrument);
            if (MinecraftForge.EVENT_BUS.post(event)) return;
            instrument = event.instrument;
            note = event.getNote();*/

            // Vanilla/Forge event
            NoteBlockEvent.Instrument vanillaInstrument = EventHelper.instrumentFromResLoc(instrument);
            if (vanillaInstrument != null) {
                NoteBlockEvent.Play e = new NoteBlockEvent.Play(world, pos, world.getBlockState(pos), note, vanillaInstrument.ordinal());
                if (MinecraftForge.EVENT_BUS.post(e)) return;
                vanillaInstrument = e.instrument;
                instrument = EventHelper.instrumentToResLoc(vanillaInstrument);
                note = e.getVanillaNoteId();
            }

            // Calculate stuff
            float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
            double x = pos.getX() + .5;
            double y = pos.getY() + .5;
            double z = pos.getZ() + .5;

            // Actually play
            world.playSoundEffect(x, y, z, instrument.toString(), volume, pitch);
            if (world instanceof WorldServer) // just in case it *isn't* being called from a WorldServer
                ((WorldServer) world).spawnParticle(EnumParticleTypes.NOTE, false, x, y + .7, z, 0, note / 24.0, 0.0, 0.0, 1.0);
        }
    }

    public static final Map<World, LinkedHashSet<NoteTickWorkaround>> map = Collections.synchronizedMap(new WeakHashMap<World, LinkedHashSet<NoteTickWorkaround>>());

    public static void addNoteTickWorkaround(World world, NoteTickWorkaround workaround) {
        synchronized (map) {
            LinkedHashSet<NoteTickWorkaround> set = map.get(world);
            if (set == null) map.put(world, set = new LinkedHashSet<NoteTickWorkaround>());
            set.add(workaround);
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.type == TickEvent.Type.WORLD) {
            synchronized (map) {
                for (Map.Entry<World, LinkedHashSet<NoteTickWorkaround>> entry : map.entrySet()) {
                    World w = entry.getKey();
                    LinkedHashSet<NoteTickWorkaround> set = entry.getValue();
                    for (NoteTickWorkaround ntw : set) {
                        ntw.play(w);
                    }
                    set.clear();
                }
            }
        }
    }
}
