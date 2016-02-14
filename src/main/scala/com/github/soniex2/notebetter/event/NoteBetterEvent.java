package com.github.soniex2.notebetter.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author soniex2
 */
public class NoteBetterEvent extends BlockEvent {
    private int note;

    NoteBetterEvent(World world, BlockPos pos, IBlockState state, int note) {
        super(world, pos, state);
        this.note = note % 25;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note % 25;
    }

    @Cancelable
    public static class Play extends NoteBetterEvent {
        public ResourceLocation instrument;

        public Play(World world, BlockPos pos, IBlockState state, int note, ResourceLocation instrument) {
            super(world, pos, state, note);
            this.instrument = instrument;
        }
    }
}
