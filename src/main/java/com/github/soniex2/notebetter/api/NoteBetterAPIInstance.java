package com.github.soniex2.notebetter.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

/**
 * @author soniex2
 */
public interface NoteBetterAPIInstance {
    @Nullable
    NoteBetterInstrument getInstrument(IBlockAccess worldInst, BlockPos blockPosInst, IBlockAccess worldNB, BlockPos blockPosNB);

    @Nullable
    NoteBetterInstrument getInstrument(IBlockState blockState, @Nullable TileEntity tileEntity);

    @Nullable
    NoteBetterInstrument getInstrument(ItemStack itemStack);

    boolean isNoteBetterInstrument(String s);
}
