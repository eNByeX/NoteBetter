package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.config.NoteBetterNoteConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.*;

@Mod(modid = NoteBetter.MODID, name = "NoteBetter", version = NoteBetter.VERSION,
        acceptedMinecraftVersions = "1.8", acceptableRemoteVersions = "*",
        guiFactory = "com.github.soniex2.notebetter.NoteBetterGuiFactory")
public class NoteBetter {
    public static final String MODID = "notebetter";
    public static final String VERSION = "0.1.0";

    @Mod.Instance
    public static NoteBetter instance;

    public File nbConfigDir;
    public NoteBetterNoteConfig defaultConfig;

    private void loadGlobalConfigs(File configDir) {
        nbConfigDir = new File(configDir, "notebetter");
        if (nbConfigDir.exists() || nbConfigDir.mkdir()) {
            File defaultConfig = new File(nbConfigDir, "default.json");
            try {
                boolean writeConf = false;
                InputStream is;
                if (defaultConfig.exists()) {
                    is = new FileInputStream(defaultConfig);
                } else {
                    writeConf = true;
                    is = getClass().getResourceAsStream("/assets/notebetter/default.json");
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bytes = new byte[4096];
                int l;
                while ((l = is.read(bytes)) != -1) {
                    baos.write(bytes, 0, l);
                }
                is.close();
                if (writeConf) {
                    OutputStream os = new FileOutputStream(defaultConfig);
                    baos.writeTo(os);
                    os.close();
                }
                String contents = baos.toString("UTF-8");
                baos.close();
                this.defaultConfig = NoteBetterNoteConfig.fromString(contents);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't create NoteBetter config!", e);
            }
        } else {
            throw new RuntimeException("Couldn't create NoteBetter config dir!");
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadGlobalConfigs(event.getModConfigurationDirectory());
    }
}
