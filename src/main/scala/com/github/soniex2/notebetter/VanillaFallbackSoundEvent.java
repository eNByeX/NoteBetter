package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.api.soundevent.IAdvancedSoundEvent;
import com.github.soniex2.notebetter.api.soundevent.RegisteredSoundEvent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;

import javax.annotation.Nullable;

/**
 * Internal sound event for vanilla fallback.
 *
 * @author soniex2
 */
public class VanillaFallbackSoundEvent implements IAdvancedSoundEvent {
    public static final VanillaFallbackSoundEvent INSTANCE = new VanillaFallbackSoundEvent();
    public static final ResourceLocation RESLOC = new ResourceLocation("notebetter:vanilla_fallback");

    private VanillaFallbackSoundEvent() {

    }

    @Override
    public void play(World world, BlockPos blockPos, SoundCategory soundCategory, float volume, int note) {
        play(world, blockPos.down(), world, blockPos, soundCategory, volume, note);
    }

    @Override
    public ResourceLocation asResourceLocation() {
        return RESLOC;
    }

    @Override
    public NoteBlockEvent.Instrument asVanillaInstrument() {
        return NoteBlockEvent.Instrument.PIANO;
    }

    private static final RegisteredSoundEvent PIANO = new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_HARP);
    private static final RegisteredSoundEvent BASS_DRUM = new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_BASEDRUM);
    private static final RegisteredSoundEvent SNARE = new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_SNARE);
    private static final RegisteredSoundEvent CLICKS = new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_HAT);
    private static final RegisteredSoundEvent DOUBLE_BASS = new RegisteredSoundEvent(SoundEvents.BLOCK_NOTE_BASS);

    @Override
    public void play(World worldInst, BlockPos blockPostInst, World worldNB, BlockPos blockPosNB, SoundCategory soundCategory, float volume, int note) {
        play(worldNB, blockPosNB, worldInst.getBlockState(blockPostInst), null, soundCategory, volume, note);
    }

    @Override
    public void play(World world, BlockPos blockPos, IBlockState blockState, @Nullable TileEntity tileEntity, SoundCategory soundCategory, float volume, int note) {
        Material m = blockState.getMaterial();
        if (m == Material.ROCK) {
            BASS_DRUM.play(world, blockPos, soundCategory, volume, note);
        } else if (m == Material.SAND) {
            SNARE.play(world, blockPos, soundCategory, volume, note);
        } else if (m == Material.GLASS) {
            CLICKS.play(world, blockPos, soundCategory, volume, note);
        } else if (m == Material.WOOD) {
            DOUBLE_BASS.play(world, blockPos, soundCategory, volume, note);
        } else {
            PIANO.play(world, blockPos, soundCategory, volume, note);
        }
    }

    @Override
    public void play(World world, BlockPos blockPos, ItemStack itemStack, SoundCategory soundCategory, float volume, int note) {
        // do nothing
    }
}
