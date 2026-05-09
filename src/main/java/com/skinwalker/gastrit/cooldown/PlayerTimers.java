package com.skinwalker.gastrit.cooldown;

import com.skinwalker.gastrit.core.TimerManager;

import java.util.UUID;

public class PlayerTimers {
    public static long set(UUID player, String key, long expireTick) {
        return TimerManager.setPlayerAt(player, key, expireTick);
    }

    public static long get(UUID player, String key) {
        return TimerManager.getPlayerExpireTick(player, key);
    }

    public static boolean isReady(UUID player, String key, long currentTick) {
        return get(player, key) <= currentTick;
    }

    public static boolean has(UUID player, String key) {
        return TimerManager.hasPlayer(player, key);
    }

    public static long remaining(UUID player, String key) {
        return TimerManager.getPlayerRemaining(player, key);
    }

    public static void remove(UUID player, String key) {
        TimerManager.cancelPlayer(player, key);
    }

    public static void clear(UUID player) {
        TimerManager.clearPlayer(player);
    }

    public static long add(UUID player, String key, long ticks) {
        return TimerManager.addTicks(player, key, ticks);
    }

    public static long reduce(UUID player, String key, long ticks) {
        return TimerManager.reduceTicks(player, key, ticks);
    }
}
