package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.event.WorkaroundHandler;
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
        WorkaroundHandler.addNoteTickWorkaround(world, new WorkaroundHandler.NoteTickWorkaround(pos, volume, note, instrument));
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
