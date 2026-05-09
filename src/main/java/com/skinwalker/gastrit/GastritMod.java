package com.skinwalker.gastrit;

import com.skinwalker.gastrit.core.TickHandler;
import com.skinwalker.gastrit.core.TimerManager;
import com.skinwalker.gastrit.handlers.NetworkHandler;
import com.skinwalker.gastrit.mappet.MappetScriptVariables;
import com.skinwalker.gastrit.mappet.client.MappetDocumentationInstaller;
import mchorse.mappet.Mappet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = GastritMod.MODID, name = GastritMod.NAME, version = GastritMod.VERSION)
public class GastritMod {
    public static final String MODID = "penis";
    public static final String NAME = "skinwClient";
    public static final String VERSION = "2.28";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    private final MappetScriptVariables mappetScriptVariables = new MappetScriptVariables();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkHandler.init();
        MinecraftForge.EVENT_BUS.register(new TickHandler());

        if (Loader.isModLoaded("mappet")) {
            Mappet.EVENT_BUS.register(this.mappetScriptVariables);

            if (FMLCommonHandler.instance().getSide().isClient()) {
                MappetDocumentationInstaller.install();
            }
        }
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        TimerManager.reset();
    }
}
