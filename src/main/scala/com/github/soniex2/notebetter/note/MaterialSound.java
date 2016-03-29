package com.github.soniex2.notebetter.note;

import com.github.soniex2.notebetter.api.NoteBetterInstrument;
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
    public ResourceLocation blockName() {
        return blockName;
    }

    @Nonnull
    public NoteBetterInstrument instrument() {
        return instrument;
    }
}
