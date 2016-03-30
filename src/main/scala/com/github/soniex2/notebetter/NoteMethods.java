package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.api.ISoundEvent;
import com.github.soniex2.notebetter.api.NoteBetterAPI;
import com.github.soniex2.notebetter.api.NoteBetterInstrument;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author soniex2
 */
public class NoteMethods {

    private static void playNote(World world, BlockPos pos, NoteBetterInstrument instrument, int note) {
        Workarounds.addNoteTickWorkaround(world, new Workarounds.NoteTickWorkaround(pos, note, instrument));
    }

    private static boolean tryPlay(World world, BlockPos pos, NoteBetterInstrument instrument, int note) {
        if (instrument != null) {
            ISoundEvent sound = instrument.iSoundEvent();
            if (sound == null) return true; // don't play anything
            playNote(world, pos, instrument, note);
            return true;
        }
        return false;
    }

    public static boolean handleTileEntity(World world, BlockPos pos) {
        if (world.getBlockState(pos.up()).getMaterial() == Material.air) {
            if (!(world.getTileEntity(pos) instanceof TileEntityNote)) return false;
            TileEntityNote te = ((TileEntityNote) world.getTileEntity(pos));
            if (tryPlay(world, pos, NoteBetterAPI.getInstrument(world, pos.down(), world, pos), te.note))
                return true;
        }
        return false;
    }
}
