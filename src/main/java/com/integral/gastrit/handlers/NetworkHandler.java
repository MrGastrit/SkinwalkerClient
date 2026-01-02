package com.integral.gastrit.handlers;

import com.integral.gastrit.network.PacketSpawnParticle;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static com.integral.gastrit.GastritMod.NETWORK;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("gastrit");

    int id = 0;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("gastrit");
        NETWORK.registerMessage(PacketSpawnParticle.Handler.class, PacketSpawnParticle.class, id++, Side.CLIENT);
    }

    public static void init() {
        INSTANCE.registerMessage(PacketSpawnParticle.Handler.class, PacketSpawnParticle.class, 0, Side.CLIENT);
    }
}
