package com.github.soniex2.notebetter.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soniex2
 */
public class NoteBetterConfigGuiScreen extends GuiConfig {
    public NoteBetterConfigGuiScreen(GuiScreen parent) {
        super(parent, getConfigElements(), "notebetter", false, false, I18n.format("notebetter.config.title"));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        return list;
    }
}
