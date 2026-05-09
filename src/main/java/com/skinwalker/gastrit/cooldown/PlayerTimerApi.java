package com.skinwalker.gastrit.cooldown;

import com.skinwalker.gastrit.core.Timer;
import com.skinwalker.gastrit.core.TimerManager;
import com.skinwalker.gastrit.mappet.MappetBridge;
import com.skinwalker.gastrit.mappet.MappetScriptTimerCallback;
import mchorse.mappet.api.scripts.user.IScriptEvent;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class PlayerTimerApi {
    public long start(Object player, String key, long delayTicks) {
        return TimerManager.setPlayer(playerId(player), key, delayTicks);
    }

    public long startAt(Object player, String key, long executeTick) {
        return TimerManager.setPlayerAt(playerId(player), key, executeTick);
    }

    public long startCallback(Object player, String key, long delayTicks, String script) {
        return startCallback(player, key, delayTicks, script, "main");
    }

    public long startCallback(Object player, String key, long delayTicks, String script, String function) {
        return TimerManager.setPlayer(playerId(player), key, delayTicks, new MappetScriptTimerCallback(script, function));
    }

    public long startCallbackAt(Object player, String key, long executeTick, String script, String function) {
        return TimerManager.setPlayerAt(playerId(player), key, executeTick, new MappetScriptTimerCallback(script, function));
    }

    public boolean has(Object player, String key) {
        return TimerManager.hasPlayer(playerId(player), key);
    }

    public boolean ready(Object player, String key) {
        return TimerManager.isPlayerReady(playerId(player), key);
    }

    public long remaining(Object player, String key) {
        return TimerManager.getPlayerRemaining(playerId(player), key);
    }

    public long expireTick(Object player, String key) {
        return TimerManager.getPlayerExpireTick(playerId(player), key);
    }

    public Timer get(Object player, String key) {
        return TimerManager.getPlayerTimer(playerId(player), key);
    }

    public void cancel(Object player, String key) {
        TimerManager.cancelPlayer(playerId(player), key);
    }

    public void clear(Object player) {
        TimerManager.clearPlayer(playerId(player));
    }

    public long start(EntityPlayerMP player, String key, long delayTicks) {
        return start((Object) player, key, delayTicks);
    }

    public long start(IScriptPlayer player, String key, long delayTicks) {
        return start((Object) player, key, delayTicks);
    }

    public long start(IScriptEvent event, String key, long delayTicks) {
        return start((Object) event, key, delayTicks);
    }

    public long reduce(Object player, String key, long ticks) {
        return TimerManager.reduceTicks(playerId(player), key, ticks);
    }

    public long add(Object player, String key, long ticks) {
        return TimerManager.addTicks(playerId(player), key, ticks);
    }

    private UUID playerId(Object player) {
        return MappetBridge.requirePlayerId(player);
    }
}
