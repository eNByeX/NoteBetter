package com.github.soniex2.notebetter.api;

import com.google.common.base.Objects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public class RegisteredSoundEvent implements ISoundEvent {
    private final SoundEvent soundEvent;

    public RegisteredSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    @Override
    public void play(World world, BlockPos blockPos, SoundCategory soundCategory, float volume, int note) {
        float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
        world.playSound(null, blockPos, soundEvent, SoundCategory.RECORDS, volume, pitch);
    }

    @Override
    public ResourceLocation asResourceLocation() {
        return soundEvent.getSoundName();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RegisteredSoundEvent)) return false;
        RegisteredSoundEvent nbi = (RegisteredSoundEvent) other;
        return Objects.equal(nbi.soundEvent, soundEvent);
    }

    @Override
    public int hashCode() {
        return asResourceLocation().hashCode();
    }

    @Override
    public String toString() {
        return asResourceLocation().toString();
    }
}
