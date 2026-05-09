package com.skinwalker.gastrit.mappet;

import com.skinwalker.gastrit.api.TimerApi;
import com.skinwalker.gastrit.cooldown.PlayerTimerApi;
import mchorse.mappet.api.scripts.user.IScriptEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class BridgeApi {
    public TimerApi server() {
        return MappetBridge.server();
    }

    public PlayerTimerApi players() {
        return MappetBridge.players();
    }

    public long now() {
        return MappetBridge.now();
    }

    public boolean execute(String script, String function, Object... args) {
        return MappetBridge.execute(script, function, args);
    }

    public boolean executeFor(Object player, String script, String function, Object... args) {
        return MappetBridge.executeFor(player, script, function, args);
    }

    public void debug(String message) {
        MappetBridge.debug(message);
    }

    public void debug(IScriptEvent event, String message) {
        MappetBridge.debug(event, message);
    }

    public UUID requirePlayerId(Object player) {
        return MappetBridge.requirePlayerId(player);
    }

    public EntityPlayerMP requirePlayer(Object player) {
        return MappetBridge.requirePlayer(player);
    }

    public EntityPlayerMP resolvePlayer(Object player) {
        return MappetBridge.resolvePlayer(player);
    }

    public EntityPlayerMP findPlayer(UUID playerId) {
        return MappetBridge.findPlayer(playerId);
    }

    public EntityPlayerMP findPlayer(String playerRef) {
        return MappetBridge.findPlayer(playerRef);
    }

    public MinecraftServer requireServer() {
        return MappetBridge.requireServer();
    }
}
