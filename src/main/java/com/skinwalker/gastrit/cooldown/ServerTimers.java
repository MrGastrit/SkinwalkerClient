package com.skinwalker.gastrit.cooldown;

import com.skinwalker.gastrit.core.TimerManager;

public class ServerTimers {
    public static long set(String key, long expireTick) {
        return TimerManager.setServerAt(key, expireTick);
    }

    public static long get(String key) {
        return TimerManager.getServerExpireTick(key);
    }

    public static boolean isReady(String key, long currentTick) {
        return get(key) <= currentTick;
    }

    public static boolean has(String key) {
        return TimerManager.hasServer(key);
    }

    public static long remaining(String key) {
        return TimerManager.getServerRemaining(key);
    }

    public static void remove(String key) {
        TimerManager.cancelServer(key);
    }

    public static long add(String key, long ticks) {
        return TimerManager.addServerTicks(key, ticks);
    }

    public static long reduce(String key, long ticks) {
        return TimerManager.reduceServerTicks(key, ticks);
    }
}
