package com.github.soniex2.notebetter.note;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author soniex2
 */
public class MaterialSound {
    @Nonnull
    protected final ResourceLocation blockName;
    @Nonnull
    protected final NoteBetterInstrument instrument;

    public MaterialSound(@Nonnull ResourceLocation blockName, @Nonnull NoteBetterInstrument instrument) {
        this.blockName = blockName;
        this.instrument = instrument;
    }

    @Nonnull
    public ResourceLocation getBlockName() {
        return blockName;
    }

    @Nonnull
    public NoteBetterInstrument getInstrument() {
        return instrument;
    }
}
