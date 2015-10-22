package com.github.soniex2.notebetter;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;

@Mod(modid = NoteBetter.MODID, name = "NoteBetter", version = NoteBetter.VERSION,
        acceptedMinecraftVersions = "1.8", acceptableRemoteVersions = "*",
        guiFactory = "com.github.soniex2.notebetter.NoteBetterGuiFactory")
public class NoteBetter {
    public static final String MODID = "notebetter";
    public static final String VERSION = "1.0.0";

    @Mod.Instance
    public static NoteBetter instance;

    public File nbConfigDir;

    private void loadGlobalConfigs(File configDir)  {
        nbConfigDir = new File(configDir, "notebetter");
        if (nbConfigDir.exists() || nbConfigDir.mkdir()) {
            File defaultConfig = new File(nbConfigDir, "default.json");
            try {
                if (defaultConfig.exists() || defaultConfig.createNewFile()) {

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Couldn't create NoteBetter config dir!");
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadGlobalConfigs(event.getModConfigurationDirectory());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
