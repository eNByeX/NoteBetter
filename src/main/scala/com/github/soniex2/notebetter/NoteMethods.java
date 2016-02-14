package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.api.NoteBetterAPI;
import com.github.soniex2.notebetter.api.NoteBetterInstrument;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * @author soniex2
 */
public class NoteMethods {

    private static void playNote(World world, BlockPos pos, ResourceLocation instrument, int note, float volume) {
        Workarounds.addNoteTickWorkaround(world, new Workarounds.NoteTickWorkaround(pos, volume, note, instrument));
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
            if (tryPlay(world, pos, NoteBetterAPI.getInstrument(world, pos.down(), world, pos), te.note))
                return true;
        }
        return false;
    }
}
