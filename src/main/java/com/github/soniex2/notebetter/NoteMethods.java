package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.note.NoteBetterInstrument;
import com.github.soniex2.notebetter.note.NoteBetterInstruments;
import com.github.soniex2.notebetter.util.EventHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class NoteMethods {

    private static void playNote(World world, BlockPos pos, ResourceLocation instrument, int note, float volume) {
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

    private static boolean tryPlay(World world, BlockPos pos, NoteBetterInstrument instrument, int note) {
        if (instrument != null) {
            ResourceLocation sound = instrument.getSoundEvent();
            if (sound == null) return true; // don't play anything
            playNote(world, pos, sound, note, instrument.getVolume());
            return true;
        }
        return false;
    }

    public static boolean handleTileEntity(World world, BlockPos pos) {
        if (world.getBlockState(pos.up()).getBlock().getMaterial() == Material.air) {
            if (!(world.getTileEntity(pos) instanceof TileEntityNote)) return false;

            TileEntityNote te = ((TileEntityNote) world.getTileEntity(pos));

            BlockPos posDown = pos.down();

            NoteBetterInstruments activeConfig = NoteBetter.instance.defaultConfig;

            /* First we do blocks */
            IBlockState blockState = world.getBlockState(posDown);
            Block block = blockState.getBlock();
            if (tryPlay(world, pos, activeConfig.getInstrumentForBlock(block), te.note))
                return true;

            /* Then we do materials */
            if (tryPlay(world, pos, activeConfig.getInstrumentForMaterial(block.getMaterial()), te.note))
                return true;

            /* Fallback to default */
            if (tryPlay(world, pos, activeConfig.getDefaultInstrument(), te.note))
                return true;
        }
        return false;
    }
}
