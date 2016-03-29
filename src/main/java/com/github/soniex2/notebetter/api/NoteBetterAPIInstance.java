package com.github.soniex2.notebetter.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author soniex2
 */
@Nonnull
public interface NoteBetterAPIInstance {
    NoteBetterInstrument getInstrument(IBlockAccess worldTarget, BlockPos blockPosTarget, IBlockAccess worldSource, BlockPos blockPosSource);

    NoteBetterInstrument getInstrument(IBlockState blockState, @Nullable TileEntity tileEntity);

    NoteBetterInstrument getInstrument(ItemStack itemStack);

    boolean isNoteBetterInstrument(String s);
}
