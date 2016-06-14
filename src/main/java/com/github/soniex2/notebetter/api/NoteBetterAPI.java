package com.github.soniex2.notebetter.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public class NoteBetterAPI {
    private static NoteBetterAPIInstance instance;

    public static void init(NoteBetterAPIInstance targetInstance) {
        if (instance != null) throw new IllegalStateException("Already initialized");
        instance = targetInstance;
    }

    /**
     * Retrieves the instrument for the block and tile entity in the given World and BlockPos. This method assumes the
     * source is 1 block above in the same world.
     *
     * @param world    The world.
     * @param blockPos The block position.
     * @return The instrument associated with the block, or {@code null} if no instrument is associated with the block.
     */
    @Nullable
    public static NoteBetterInstrument getInstrument(World world, BlockPos blockPos) {
        return instance.getInstrument(world, blockPos, world, blockPos.up());
    }

    /**
     * Retrieves the instrument for the block and tile entity in the given World and BlockPos, relative to the
     * consumer's World and BlockPos. This method is used for inter-world instrument retrieval.
     *
     * @param worldInst    The target's world.
     * @param blockPosInst The target's block position.
     * @param worldNB      The consumer's world.
     * @param blockPosNB   The consumer's block position.
     * @return The instrument associated with the block, or {@code null} if no instrument is associated with the block.
     */
    @Nullable
    public static NoteBetterInstrument getInstrument(World worldInst, BlockPos blockPosInst, World worldNB, BlockPos blockPosNB) {
        return instance.getInstrument(worldInst, blockPosInst, worldNB, blockPosNB);
    }

    /**
     * Retrieves the instrument for the given block and block state.
     *
     * @param blockState The block state.
     * @return The instrument associated with the block, or {@code null} if no instrument is associated with the block.
     */
    @Nullable
    public static NoteBetterInstrument getInstrument(IBlockState blockState) {
        return instance.getInstrument(blockState, null);
    }

    /**
     * Retrieves the instrument for the given block, block state and tile entity.
     *
     * @param blockState The block state.
     * @param tileEntity The tile entity.
     * @return The instrument associated with the block, or {@code null} if no instrument is associated with the block.
     */
    @Nullable
    public static NoteBetterInstrument getInstrument(IBlockState blockState, @Nullable TileEntity tileEntity) {
        return instance.getInstrument(blockState, tileEntity);
    }

    /**
     * EXPERIMENTAL: Retrieves the instrument for the given ItemStack.
     *
     * @param is The ItemStack.
     * @return The instrument associated with the item, or {@code null} if no instrument is associated with the item.
     */
    @Nullable
    public static NoteBetterInstrument getInstrument(ItemStack is) {
        return instance.getInstrument(is);
    }

    /**
     * EXPERIMENTAL: Checks if the given string is a valid/registered instrument.
     * @param s The string.
     * @return {@code true} if the given string is known to be a NoteBetter instrument.
     */
    public static boolean isNoteBetterInstrument(String s) {
        return instance.isNoteBetterInstrument(s);
    }
}
