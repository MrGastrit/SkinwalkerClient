package com.skinwalker.gastrit.mappet.client;

import com.skinwalker.gastrit.GastritMod;
import mchorse.mappet.CommonProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SideOnly(Side.CLIENT)
public class MappetDocumentationInstaller {
    private static final String RESOURCE_PATH = "/documentation/Timers.json";
    private static final String OUTPUT_DIRECTORY = "documentation/skinwclient";
    private static final String OUTPUT_FILE = "skinwclient_docs.json";

    private MappetDocumentationInstaller() {
    }

    public static void install() {
        if (CommonProxy.configFolder == null) {
            GastritMod.LOGGER.warn("Mappet config folder is not available, script documentation was not installed");
            return;
        }

        File directory = new File(CommonProxy.configFolder, OUTPUT_DIRECTORY);
        File output = new File(directory, OUTPUT_FILE);

        if (!directory.exists() && !directory.mkdirs()) {
            GastritMod.LOGGER.warn("Failed to create Mappet documentation directory at {}", directory.getAbsolutePath());
            return;
        }

        try (InputStream stream = MappetDocumentationInstaller.class.getResourceAsStream(RESOURCE_PATH)) {
            if (stream == null) {
                GastritMod.LOGGER.warn("Bundled Mappet documentation resource {} was not found", RESOURCE_PATH);
                return;
            }

            Files.copy(stream, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            GastritMod.LOGGER.error("Failed to install Mappet documentation", e);
        }
    }
}
