package com.github.soniex2.notebetter.config.gui.elements;

import net.minecraftforge.fml.client.config.GuiEditArray;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * @author soniex2
 */
public class GuiEditJSONArrayEntries extends GuiEditArrayEntries.BaseEntry {

    public GuiEditJSONArrayEntries(GuiEditArray owningScreen, GuiEditArrayEntries owningEntryList,
                                   IConfigElement configElement, Object value) {
        super(owningScreen, owningEntryList, configElement);
    }
}
