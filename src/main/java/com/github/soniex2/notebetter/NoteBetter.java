package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.event.WorkaroundHandler;
import com.github.soniex2.notebetter.note.NoteBetterInstruments;
import com.github.soniex2.notebetter.util.StreamHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.*;

@Mod(modid = NoteBetter.MODID, name = "NoteBetter", version = NoteBetter.VERSION,
        acceptedMinecraftVersions = "1.8.*", acceptableRemoteVersions = "*",
        guiFactory = "com.github.soniex2.notebetter.gui.NoteBetterGuiFactory")
public class NoteBetter {
    public static final String MODID = "notebetter";
    public static final String VERSION = "0.2.3";

    @Mod.Instance
    public static NoteBetter instance;

    public File nbConfigDir;
    public NoteBetterInstruments defaultConfig;

    public Logger log;

    private void loadGlobalConfigs() {
        File defaultConfig = new File(nbConfigDir, "default.json");
        try {
            boolean writeConf = false;
            InputStream is;
            if (defaultConfig.exists()) {
                is = new FileInputStream(defaultConfig);
            } else {
                writeConf = true;
                is = getClass().getResourceAsStream("/assets/notebetter/config/default.json");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamHelper.copy(is, baos);
            is.close();
            if (writeConf) {
                OutputStream os = new FileOutputStream(defaultConfig);
                baos.writeTo(os);
                os.close();
            }
            String contents = baos.toString("UTF-8");
            baos.close();
            this.defaultConfig = NoteBetterInstruments.fromString(contents);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create NoteBetter config!", e);
        }
    }

    private void unpackExamples() {
        File examplesDir = new File(nbConfigDir, "examples");
        if (!examplesDir.exists() && !examplesDir.mkdir()) {
            log.warn("Couldn't create examples dir. Not unpacking examples.");
            return;
        }
        try {
            File readmef = new File(examplesDir, "README.txt");
            if (readmef.createNewFile()) {
                InputStream readmeis = getClass().getResourceAsStream("/assets/notebetter/config/examples/README.txt");
                if (readmeis != null) {
                    OutputStream readmeos = new FileOutputStream(readmef);
                    StreamHelper.copy(readmeis, readmeos);
                    readmeis.close();
                    readmeos.close();
                } else {
                    log.warn("Couldn't find a README.txt to unpack.");
                }
            }
        } catch (IOException e) {
            log.warn("Couldn't create exmaples/README.txt.", e);
        }
        InputStream list = getClass().getResourceAsStream("/assets/notebetter/config/examples/list.txt");
        if (list == null) {
            log.warn("Couldn't find examples list. Not unpacking examples.");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(list));
        try {
            for (String s; (s = reader.readLine()) != null; ) {
                s = s.trim();
                if (s.isEmpty()) continue;
                if (!s.matches("^[a-z_][a-z_0-9]*$")) continue; // compatibility reasons
                String fname = s + ".json";
                File exampleFile = new File(examplesDir, fname);
                try {
                    if (!exampleFile.createNewFile()) continue;
                    InputStream example = getClass().getResourceAsStream("/assets/notebetter/config/examples/" + fname);
                    if (example == null) continue;
                    OutputStream out = new FileOutputStream(exampleFile);
                    StreamHelper.copy(example, out);
                    example.close();
                    out.close();
                } catch (IOException e) {
                    log.warn("Couldn't read/write example \"" + s + "\".", e);
                }
            }
            list.close();
        } catch (IOException e) {
            log.warn("Couldn't read examples list.", e);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();

        nbConfigDir = new File(event.getModConfigurationDirectory(), "notebetter");
        if (!nbConfigDir.exists() && !nbConfigDir.mkdir())
            throw new RuntimeException("Couldn't create NoteBetter config dir!");

        loadGlobalConfigs();
        unpackExamples();

        MinecraftForge.EVENT_BUS.register(new WorkaroundHandler());
    }
}
