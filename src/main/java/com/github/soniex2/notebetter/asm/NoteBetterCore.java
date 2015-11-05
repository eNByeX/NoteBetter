package com.github.soniex2.notebetter.asm;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * @author soniex2
 */
@IFMLLoadingPlugin.MCVersion("1.8")
@IFMLLoadingPlugin.TransformerExclusions("com.github.soniex2.notebetter")
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
public class NoteBetterCore implements IFMLLoadingPlugin {
    /**
     * @inheritDoc
     */
    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "com.github.soniex2.notebetter.asm.NoteBetterClassTransformer"
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getModContainerClass() {
        return null;//"com.github.soniex2.notebetter.asm.NoteBetterCore$NoteBetterCoreModContainer";
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getSetupClass() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void injectData(Map<String, Object> data) {

    }

    /**
     * @inheritDoc
     */
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
