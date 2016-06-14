package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.api.soundevent.ISoundEvent;
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

    private static boolean playNote(World world, BlockPos pos, NoteBetterInstrument instrument, int note) {
        Workarounds.addNoteTickWorkaround(world, new Workarounds.NoteTickWorkaround(pos, note, instrument));
        return true;
    }

    private static boolean tryPlay(World world, BlockPos pos, NoteBetterInstrument instrument, int note) {
        if (instrument != null) {
            ISoundEvent sound = instrument.soundEvent();
            if (sound == null) return true; // don't play anything
            return playNote(world, pos, instrument, note);
        }
        return false;
    }

    public static boolean handleTileEntity(World world, BlockPos pos) {
        if (world.getBlockState(pos.up()).getMaterial() == Material.AIR) {
            if (!(world.getTileEntity(pos) instanceof TileEntityNote)) return false;
            TileEntityNote te = ((TileEntityNote) world.getTileEntity(pos));
            if (tryPlay(world, pos, NoteBetterAPI.getInstrument(world, pos.down(), world, pos), te.note))
                return true;
        }
        return false;
    }
}
