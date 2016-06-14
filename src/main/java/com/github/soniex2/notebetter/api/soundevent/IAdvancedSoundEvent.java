package com.github.soniex2.notebetter.api.soundevent;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;

import javax.annotation.Nullable;

/**
 * Source-sensitive sound event.
 *
 * @author soniex2
 */
// TODO java8: swap IAdvancedSoundEvent and ISoundEvent, use default methods to implement IAdvancedSoundEvent in ISoundEvent.
public interface IAdvancedSoundEvent extends ISoundEvent {
    void play(World worldInst, BlockPos blockPostInst, World worldNB, BlockPos blockPosNB, SoundCategory soundCategory, float volume, int note);

    void play(World world, BlockPos blockPos, IBlockState blockState, @Nullable TileEntity tileEntity, SoundCategory soundCategory, float volume, int note);

    void play(World world, BlockPos blockPos, ItemStack itemStack, SoundCategory soundCategory, float volume, int note);

    // Helper class
    class Wrapper implements IAdvancedSoundEvent {
        private final ISoundEvent iSoundEvent;
        private final boolean isAdvanced;

        public Wrapper(ISoundEvent iSoundEvent) {
            this.iSoundEvent = iSoundEvent;
            isAdvanced = iSoundEvent instanceof IAdvancedSoundEvent;
        }


        @Override
        public void play(World worldInst, BlockPos blockPostInst, World worldNB, BlockPos blockPosNB, SoundCategory soundCategory, float volume, int note) {
            if (isAdvanced) {
                ((IAdvancedSoundEvent) iSoundEvent).play(worldInst, blockPostInst, worldNB, blockPosNB, soundCategory, volume, note);
            } else {
                iSoundEvent.play(worldInst, blockPostInst, soundCategory, volume, note);
            }
        }

        @Override
        public void play(World world, BlockPos blockPos, IBlockState blockState, @Nullable TileEntity tileEntity, SoundCategory soundCategory, float volume, int note) {
            if (isAdvanced) {
                ((IAdvancedSoundEvent) iSoundEvent).play(world, blockPos, blockState, tileEntity, soundCategory, volume, note);
            } else {
                iSoundEvent.play(world, blockPos, soundCategory, volume, note);
            }
        }

        @Override
        public void play(World world, BlockPos blockPos, ItemStack itemStack, SoundCategory soundCategory, float volume, int note) {
            if (isAdvanced) {
                ((IAdvancedSoundEvent) iSoundEvent).play(world, blockPos, itemStack, soundCategory, volume, note);
            } else {
                iSoundEvent.play(world, blockPos, soundCategory, volume, note);
            }
        }

        @Override
        public void play(World world, BlockPos blockPos, SoundCategory soundCategory, float volume, int note) {
            iSoundEvent.play(world, blockPos, soundCategory, volume, note);
        }

        @Override
        public ResourceLocation asResourceLocation() {
            return iSoundEvent.asResourceLocation();
        }

        @Override
        public NoteBlockEvent.Instrument asVanillaInstrument() {
            return iSoundEvent.asVanillaInstrument();
        }
    }
}
