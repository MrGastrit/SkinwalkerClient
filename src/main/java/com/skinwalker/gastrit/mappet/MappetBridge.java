package com.skinwalker.gastrit.mappet;

import com.skinwalker.gastrit.GastritMod;
import com.skinwalker.gastrit.api.TimerApi;
import com.skinwalker.gastrit.cooldown.PlayerTimerApi;
import com.skinwalker.gastrit.core.Timer;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.user.IScriptEvent;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.script.ScriptException;
import java.util.UUID;

public class MappetBridge {
    private static final TimerApi SERVER_TIMERS = new TimerApi();
    private static final PlayerTimerApi PLAYER_TIMERS = new PlayerTimerApi();

    public static TimerApi server() {
        return SERVER_TIMERS;
    }

    public static PlayerTimerApi players() {
        return PLAYER_TIMERS;
    }

    public static long now() {
        return SERVER_TIMERS.now();
    }

    public static boolean execute(String script, String function, Object... args) {
        return execute(script, function, new DataContext(requireServer()), args);
    }

    public static boolean executeFor(Object player, String script, String function, Object... args) {
        EntityPlayerMP resolvedPlayer = requirePlayer(player);

        return execute(script, function, new DataContext(resolvedPlayer), args);
    }

    static boolean executeForTimer(Timer timer, String script, String function) {
        if (timer.isPlayerTimer()) {
            EntityPlayerMP player = findPlayer(timer.getPlayerId());

            if (player != null) {
                return execute(script, function, new DataContext(player), timer);
            }

            GastritMod.LOGGER.warn("Player {} is offline, timer callback {}::{} will run in server context", timer.getPlayerId(), script, function);
        }

        return execute(script, function, new DataContext(requireServer()), timer);
    }

    public static boolean execute(String script, String function, DataContext context, Object... args) {
        if (Mappet.scripts == null) {
            GastritMod.LOGGER.error("Mappet ScriptManager is not available");
            return false;
        }

        try {
            Mappet.scripts.execute(script, function, context, buildArguments(context, script, function, args));
            return true;
        } catch (ScriptException e) {
            GastritMod.LOGGER.error("Mappet script error in {}::{}", script, function, e);
        } catch (NoSuchMethodException e) {
            GastritMod.LOGGER.error("Mappet function {}::{} was not found", script, function, e);
        } catch (Exception e) {
            GastritMod.LOGGER.error("Unexpected error while running {}::{}", script, function, e);
        }

        return false;
    }

    public static void debug(String message) {
        GastritMod.LOGGER.info("[MappetBridge] {}", message);
    }

    public static void debug(IScriptEvent event, String message) {
        if (event != null) {
            event.send(message);
        }

        debug(message);
    }

    public static UUID requirePlayerId(Object player) {
        EntityPlayerMP resolved = resolvePlayer(player);

        if (resolved != null) {
            return resolved.getUniqueID();
        }

        if (player instanceof UUID) {
            return (UUID) player;
        }

        throw new IllegalArgumentException("Couldn't resolve player from " + describe(player));
    }

    public static EntityPlayerMP requirePlayer(Object player) {
        EntityPlayerMP resolved = resolvePlayer(player);

        if (resolved == null) {
            throw new IllegalArgumentException("Couldn't resolve player from " + describe(player));
        }

        return resolved;
    }

    public static EntityPlayerMP resolvePlayer(Object player) {
        if (player == null) {
            return null;
        }

        if (player instanceof EntityPlayerMP) {
            return (EntityPlayerMP) player;
        }

        if (player instanceof IScriptPlayer) {
            return ((IScriptPlayer) player).getMinecraftPlayer();
        }

        if (player instanceof IScriptEvent) {
            IScriptPlayer scriptPlayer = ((IScriptEvent) player).getPlayer();
            return scriptPlayer == null ? null : scriptPlayer.getMinecraftPlayer();
        }

        if (player instanceof UUID) {
            return findPlayer((UUID) player);
        }

        if (player instanceof String) {
            return findPlayer((String) player);
        }

        return null;
    }

    public static EntityPlayerMP findPlayer(UUID playerId) {
        MinecraftServer server = requireServer();

        return server.getPlayerList().getPlayerByUUID(playerId);
    }

    public static EntityPlayerMP findPlayer(String playerRef) {
        if (playerRef == null) {
            return null;
        }

        MinecraftServer server = requireServer();
        String normalized = playerRef.trim();

        if (normalized.isEmpty()) {
            return null;
        }

        try {
            return server.getPlayerList().getPlayerByUUID(UUID.fromString(normalized));
        } catch (IllegalArgumentException e) {
            return server.getPlayerList().getPlayerByUsername(normalized);
        }
    }

    public static MinecraftServer requireServer() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server == null) {
            throw new IllegalStateException("Minecraft server is not available");
        }

        return server;
    }

    private static String describe(Object value) {
        return value == null ? "null" : value.getClass().getName();
    }

    private static Object[] buildArguments(DataContext context, String script, String function, Object... args) {
        Object[] extraArgs = args == null ? new Object[0] : args;
        Object[] finalArgs = new Object[extraArgs.length + 1];

        finalArgs[0] = new ScriptEvent(context, script, function);
        System.arraycopy(extraArgs, 0, finalArgs, 1, extraArgs.length);

        return finalArgs;
    }
}
