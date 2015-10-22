package com.github.soniex2.notebetter.config.elements;

import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author soniex2
 */
public class ObjectConfigElement implements IConfigElement {
    // TODO

    /**
     * [Property, Category] Is this object a property object?
     */
    @Override
    public boolean isProperty() {
        return true;
    }

    /**
     * This method returns a class that implements {@code IConfigEntry} or null. This class MUST
     * provide a constructor with the following parameter types: {@code GuiConfig}, {@code GuiConfigEntries}, {@code IConfigElement}
     */
    @Override
    public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
        return null;
    }

    /**
     * This method returns a class that implements {@code IArrayEntry}. This class MUST provide a constructor with the
     * following parameter types: {@code GuiEditArray}, {@code GuiEditArrayEntries}, {@code IConfigElement}, {@code Object}
     */
    @Override
    public Class<? extends GuiEditArrayEntries.IArrayEntry> getArrayEntryClass() {
        return null;
    }

    /**
     * [Property, Category] Gets the name of this object.
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * [Category] Gets the qualified name of this object. This is typically only used for category objects.
     */
    @Override
    public String getQualifiedName() {
        return null;
    }

    /**
     * [Property, Category] Gets a language key for localization of config GUI entry names. If the same key is specified with .tooltip
     * appended to the end, that key will return a localized tooltip when the mouse hovers over the property label/category button.
     */
    @Override
    public String getLanguageKey() {
        return null;
    }

    /**
     * [Property, Category] Gets the comment for this object. Used for the tooltip if getLanguageKey() + ".tooltip" is not defined in the
     * .lang file.
     */
    @Override
    public String getComment() {
        return null;
    }

    /**
     * [Category] Gets this category's child categories/properties.
     */
    @Override
    public List<IConfigElement> getChildElements() {
        return null;
    }

    /**
     * [Property, Category] Gets the ConfigGuiType value corresponding to the type of this property object, or CONFIG_CATEGORY if this is a
     * category object.
     */
    @Override
    public ConfigGuiType getType() {
        return null;
    }

    /**
     * [Property] Is this property object a list?
     */
    @Override
    public boolean isList() {
        return false;
    }

    /**
     * [Property] Does this list property have to remain a fixed length?
     */
    @Override
    public boolean isListLengthFixed() {
        return false;
    }

    /**
     * [Property] Gets the max length of this list property, or -1 if the length is unlimited.
     */
    @Override
    public int getMaxListLength() {
        return 0;
    }

    /**
     * [Property] Is this property value equal to the default value?
     */
    @Override
    public boolean isDefault() {
        return false;
    }

    /**
     * [Property] Gets this property's default value. If this element is an array, this method should return a String
     * representation of that array using Arrays.toString()
     */
    @Override
    public Object getDefault() {
        return null;
    }

    /**
     * [Property] Gets this property's default values.
     */
    @Override
    public Object[] getDefaults() {
        return new Object[0];
    }

    /**
     * [Property] Sets this property's value to the default value.
     */
    @Override
    public void setToDefault() {

    }

    /**
     * [Property, Category] Whether or not this element is safe to modify while a world is running. For Categories return false if ANY properties
     * in the category are modifiable while a world is running, true if all are not.
     */
    @Override
    public boolean requiresWorldRestart() {
        return false;
    }

    /**
     * [Property, Category] Whether or not this element should be allowed to show on config GUIs.
     */
    @Override
    public boolean showInGui() {
        return false;
    }

    /**
     * [Property, Category] Whether or not this element requires Minecraft to be restarted when changed.
     */
    @Override
    public boolean requiresMcRestart() {
        return false;
    }

    /**
     * [Property] Gets this property value.
     */
    @Override
    public Object get() {
        return null;
    }

    /**
     * [Property] Gets this property value as a list. Generally you should be sure of whether the property is a list before calling this.
     */
    @Override
    public Object[] getList() {
        return new Object[0];
    }

    /**
     * [Property] Sets this property's value.
     *
     * @param value
     */
    @Override
    public void set(Object value) {

    }

    /**
     * [Property] Sets this property's value to the specified array.
     *
     * @param aVal
     */
    @Override
    public void set(Object[] aVal) {

    }

    /**
     * [Property] Gets a String array of valid values for this property. This is generally used for String properties to allow the user to
     * select a value from a list of valid values.
     */
    @Override
    public String[] getValidValues() {
        return new String[0];
    }

    /**
     * [Property] Gets this property's minimum value.
     */
    @Override
    public Object getMinValue() {
        return null;
    }

    /**
     * [Property] Gets this property's maximum value.
     */
    @Override
    public Object getMaxValue() {
        return null;
    }

    /**
     * [Property] Gets a Pattern object used in String property input validation.
     */
    @Override
    public Pattern getValidationPattern() {
        return null;
    }
}
