package com.github.soniex2.notebetter.gui;

import com.github.soniex2.notebetter.config.gui.NoteBetterConfigGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.List;
import java.util.Set;

/**
 * @author soniex2
 */
public class NoteBetterGuiFactory implements IModGuiFactory {

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return NoteBetterConfigGuiScreen.class;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return new RuntimeOptionGuiHandler() {
            /**
             * @inheritDoc
             */
            @Override
            public void addWidgets(List<Gui> widgetList, int x, int y, int w, int h) {

            }

            /**
             * @inheritDoc
             */
            @Override
            public void paint(int x, int y, int w, int h) {

            }

            /**
             * @inheritDoc
             */
            @Override
            public void actionCallback(int actionId) {

            }

            /**
             * @inheritDoc
             */
            @Override
            public void close() {

            }
        };
    }
}
