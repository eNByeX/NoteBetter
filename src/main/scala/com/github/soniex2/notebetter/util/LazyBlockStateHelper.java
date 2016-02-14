package com.github.soniex2.notebetter.util;

import com.google.common.base.Predicate;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.util.ResourceLocation;

/**
 * @author soniex2
 */
public class LazyBlockStateHelper implements Predicate<IBlockState> {
    private BlockStateHelper predicate;
    private ResourceLocation block;
    private JsonObject partialState;

    public LazyBlockStateHelper(ResourceLocation block, JsonObject partialState) {
        this.block = block;
        this.partialState = partialState;
    }

    @Override
    public boolean apply(IBlockState state) {
        if (predicate != null)
            return predicate.apply(state);
        predicate = BlockStateHelper.forBlock(Block.blockRegistry.getObject(block));
        // TODO partialState
        block = null;
        partialState = null;
        return predicate.apply(state);
    }
}
