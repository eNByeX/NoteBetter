package com.github.soniex2.notebetter.api;

import com.google.common.base.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * @author soniex2
 */
public class CustomSoundEvent implements ISoundEvent {
    private final ResourceLocation soundEvent;

    public CustomSoundEvent(ResourceLocation soundEvent) {
        this.soundEvent = soundEvent;
    }

    @Override
    public void play(World world, BlockPos blockPos, SoundCategory soundCategory, float volume, int note) {
        // This is such a pain.
        if (!(world instanceof WorldServer)) return;
        float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
        double x = blockPos.getX() + .5;
        double y = blockPos.getY() + .5;
        double z = blockPos.getZ() + .5;
        SPacketCustomSound packet = new SPacketCustomSound(soundEvent.toString(), soundCategory, x, y, z, volume, pitch);
        world.getMinecraftServer().getPlayerList().sendToAllNearExcept(null, x, y, z, volume > 1f ? (16. * volume) : 16., world.provider.getDimensionType().getId(), packet);
        ((WorldServer) world).spawnParticle(EnumParticleTypes.NOTE, false, x, y + .7, z, 0, note / 24.0, 0.0, 0.0, 1.0);
    }

    @Override
    public ResourceLocation asResourceLocation() {
        return soundEvent;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CustomSoundEvent)) return false;
        CustomSoundEvent nbi = (CustomSoundEvent) other;
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
