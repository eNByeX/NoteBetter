package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.util.EventHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;

import java.util.*;

/**
 * @author soniex2
 */
public class Workarounds {
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

    public static final Map<World, NoteTickWorkaroundWorldData> map = Collections.synchronizedMap(new WeakHashMap<World, NoteTickWorkaroundWorldData>());

    public static void addNoteTickWorkaround(World world, NoteTickWorkaround workaround) {
        NoteTickWorkaroundWorldData data;
        synchronized (map) {
            data = map.get(world);
            if (data == null)
                map.put(world, data = new NoteTickWorkaroundWorldData());
        }
        data.add(workaround);
    }

    // This needs to happen between the start of the tick and TileEntity updates, but also after block ticks.
    public static void sendNoteUpdates(World world) {
        NoteTickWorkaroundWorldData data = map.get(world);
        if (data == null) return;
        data.sendQueuedBlockEvents(world);
    }

    private static class NoteTickWorkaroundWorldData {
        private int blockEventCacheIndex = 0;
        @SuppressWarnings("unchecked")
        private List<NoteTickWorkaround>[] blockEventCaches = new ArrayList[] {
                new ArrayList<NoteTickWorkaround>(), new ArrayList<NoteTickWorkaround>()
        };

        public synchronized void add(NoteTickWorkaround workaround) {
            for (NoteTickWorkaround blockeventdata1 : blockEventCaches[blockEventCacheIndex]) {
                if (blockeventdata1.equals(workaround)) return;
            }

            blockEventCaches[blockEventCacheIndex].add(workaround);
        }

        private synchronized void sendQueuedBlockEvents(World w) {
            while (!blockEventCaches[blockEventCacheIndex].isEmpty()) {
                int i = blockEventCacheIndex;
                blockEventCacheIndex ^= 1;

                for (NoteTickWorkaround blockeventdata : blockEventCaches[i]) {
                    blockeventdata.play(w);
                }

                this.blockEventCaches[i].clear();
            }
        }
    }
}
