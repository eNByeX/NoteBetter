package com.github.soniex2.notebetter.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author soniex2
 */
public interface ISoundEvent {
    void play(World world, BlockPos blockPos, SoundCategory soundCategory, float volume, int note);

    ResourceLocation asResourceLocation();
}
