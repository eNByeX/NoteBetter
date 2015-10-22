package com.github.soniex2.notebetter.config.elements;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author soniex2
 */
public class ArrayConfigElement implements IConfigElement {
    protected String name;
    protected String langKey;
    protected Object[] defaultValues;
    protected Object[] values;

    /**
     * @inheritDoc
     */
    @Override
    public boolean isProperty() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
        return GuiConfigEntries.ArrayEntry.class;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<? extends GuiEditArrayEntries.IArrayEntry> getArrayEntryClass() {
        return GuiEditJSONArrayEntries.class;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getQualifiedName() {
        return name;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getLanguageKey() {
        return langKey;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getComment() {
        return I18n.format(langKey + ".tooltip");
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<IConfigElement> getChildElements() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ConfigGuiType getType() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isList() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isListLengthFixed() {
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getMaxListLength() {
        return -1;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isDefault() {
        return Arrays.deepEquals(values, defaultValues);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getDefault() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object[] getDefaults() {
        return defaultValues;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setToDefault() {
        // TODO
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean requiresWorldRestart() {
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean showInGui() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean requiresMcRestart() {
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object get() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object[] getList() {
        return values;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void set(Object value) {

    }

    /**
     * @inheritDoc
     */
    @Override
    public void set(Object[] aVal) {
        values = aVal;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getValidValues() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getMinValue() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getMaxValue() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Pattern getValidationPattern() {
        return null;
    }
}
