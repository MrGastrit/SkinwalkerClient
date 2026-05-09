package com.skinwalker.gastrit.core;

import com.skinwalker.gastrit.core.callback.ITimerCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TimerManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private static long currentTick = 0;
    private static final Map<Long, List<Timer>> buckets = new HashMap<>();
    private static final Map<String, Timer> serverTimers = new HashMap<>();
    private static final Map<UUID, Map<String, Timer>> playerTimers = new HashMap<>();

    private TimerManager() {
    }

    public static long getCurrentTick() {
        return currentTick;
    }

    public static void tick() {
        currentTick++;

        List<Timer> dueTimers = buckets.remove(currentTick);

        if (dueTimers == null || dueTimers.isEmpty()) {
            return;
        }

        for (Timer timer : dueTimers) {
            expire(timer);
        }
    }

    public static void reset() {
        currentTick = 0;
        buckets.clear();
        serverTimers.clear();
        playerTimers.clear();
    }

    public static long setServer(String key, long delayTicks) {
        return setServer(key, delayTicks, null);
    }

    public static long setServer(String key, long delayTicks, ITimerCallback callback) {
        return setServerAt(key, currentTick + normalizeDelay(delayTicks), callback);
    }

    public static long setServerAt(String key, long executeTick) {
        return setServerAt(key, executeTick, null);
    }

    public static long setServerAt(String key, long executeTick, ITimerCallback callback) {
        Timer previous = getServerTimer(key);

        if (previous != null) {
            removeActiveTimer(previous);
        }

        Timer timer = createTimer(requireKey(key), TimerScope.SERVER, null, normalizeExecuteTick(executeTick), callback);
        serverTimers.put(timer.getKey(), timer);
        addToBucket(timer);

        return timer.getExecuteTick();
    }

    public static boolean hasServer(String key) {
        return serverTimers.containsKey(requireKey(key));
    }

    public static boolean isServerReady(String key) {
        return getServerRemaining(key) <= 0;
    }

    public static long getServerRemaining(String key) {
        Timer timer = serverTimers.get(requireKey(key));

        return timer == null ? 0 : Math.max(0, timer.getExecuteTick() - currentTick);
    }

    public static long getServerExpireTick(String key) {
        Timer timer = serverTimers.get(requireKey(key));

        return timer == null ? 0 : timer.getExecuteTick();
    }

    public static Timer getServerTimer(String key) {
        return serverTimers.get(requireKey(key));
    }

    public static void cancelServer(String key) {
        Timer timer = getServerTimer(key);

        if (timer != null) {
            removeActiveTimer(timer);
        }
    }

    public static long setPlayer(UUID playerId, String key, long delayTicks) {
        return setPlayer(playerId, key, delayTicks, null);
    }

    public static long setPlayer(UUID playerId, String key, long delayTicks, ITimerCallback callback) {
        return setPlayerAt(playerId, key, currentTick + normalizeDelay(delayTicks), callback);
    }

    public static long setPlayerAt(UUID playerId, String key, long executeTick) {
        return setPlayerAt(playerId, key, executeTick, null);
    }

    public static long setPlayerAt(UUID playerId, String key, long executeTick, ITimerCallback callback) {
        UUID requiredPlayerId = requirePlayerId(playerId);
        Timer previous = getPlayerTimer(requiredPlayerId, key);

        if (previous != null) {
            removeActiveTimer(previous);
        }

        Timer timer = createTimer(requireKey(key), TimerScope.PLAYER, requiredPlayerId, normalizeExecuteTick(executeTick), callback);

        playerTimers.computeIfAbsent(requiredPlayerId, id -> new HashMap<>()).put(timer.getKey(), timer);
        addToBucket(timer);

        return timer.getExecuteTick();
    }

    public static boolean hasPlayer(UUID playerId, String key) {
        return getPlayerTimer(playerId, key) != null;
    }

    public static boolean isPlayerReady(UUID playerId, String key) {
        return getPlayerRemaining(playerId, key) <= 0;
    }

    public static long getPlayerRemaining(UUID playerId, String key) {
        Timer timer = getPlayerTimer(playerId, key);

        return timer == null ? 0 : Math.max(0, timer.getExecuteTick() - currentTick);
    }

    public static long getPlayerExpireTick(UUID playerId, String key) {
        Timer timer = getPlayerTimer(playerId, key);

        return timer == null ? 0 : timer.getExecuteTick();
    }

    public static Timer getPlayerTimer(UUID playerId, String key) {
        Map<String, Timer> timers = playerTimers.get(requirePlayerId(playerId));

        return timers == null ? null : timers.get(requireKey(key));
    }

    public static void cancelPlayer(UUID playerId, String key) {
        UUID requiredPlayerId = requirePlayerId(playerId);
        Map<String, Timer> timers = playerTimers.get(requiredPlayerId);

        if (timers == null) {
            return;
        }

        timers.remove(requireKey(key));

        if (timers.isEmpty()) {
            playerTimers.remove(requiredPlayerId);
        }
    }

    public static void clearPlayer(UUID playerId) {
        playerTimers.remove(requirePlayerId(playerId));
    }


    public static Map<String, Timer> getPlayerTimers(UUID playerId) {
        Map<String, Timer> timers = playerTimers.get(requirePlayerId(playerId));

        return timers == null ? Collections.<String, Timer>emptyMap() : Collections.unmodifiableMap(timers);
    }

    public static long reduceServerTicks(String key, long ticks) {
        return moveServerTimer(key, -normalizeShiftTicks(ticks));
    }

    public static long addServerTicks(String key, long ticks) {
        return moveServerTimer(key, normalizeShiftTicks(ticks));
    }

    public static long reduceTicks(UUID playerId, String key, long ticks) {
        return movePlayerTimer(playerId, key, -normalizeShiftTicks(ticks));
    }

    public static long addTicks(UUID playerId, String key, long ticks) {
        return movePlayerTimer(playerId, key, normalizeShiftTicks(ticks));
    }

    private static long moveServerTimer(String key, long delta) {
        Timer timer = getServerTimer(key);

        if (timer == null) {
            return 0;
        }

        long executeTick = normalizeExecuteTick(timer.getExecuteTick() + delta);

        return setServerAt(timer.getKey(), executeTick, timer.getCallback());
    }

    private static long movePlayerTimer(UUID playerId, String key, long delta) {
        Timer timer = getPlayerTimer(playerId, key);

        if (timer == null) {
            return 0;
        }

        long executeTick = normalizeExecuteTick(timer.getExecuteTick() + delta);

        return setPlayerAt(playerId, timer.getKey(), executeTick, timer.getCallback());
    }

    private static Timer createTimer(String key, TimerScope scope, UUID playerId, long executeTick, ITimerCallback callback) {
        return new Timer(key, scope, playerId, currentTick, executeTick, callback);
    }

    private static void addToBucket(Timer timer) {
        buckets.computeIfAbsent(timer.getExecuteTick(), tick -> new ArrayList<>()).add(timer);
    }

    private static void expire(Timer timer) {
        if (!isCurrent(timer)) {
            return;
        }

        removeActiveTimer(timer);

        ITimerCallback callback = timer.getCallback();

        if (callback == null) {
            return;
        }

        try {
            callback.execute(timer);
        } catch (Exception e) {
            LOGGER.error("Failed to execute timer callback for {}", timer.getKey(), e);
        }
    }

    private static boolean isCurrent(Timer timer) {
        if (timer.isPlayerTimer()) {
            Map<String, Timer> timers = playerTimers.get(timer.getPlayerId());

            return timers != null && timers.get(timer.getKey()) == timer;
        }

        return serverTimers.get(timer.getKey()) == timer;
    }

    private static void removeActiveTimer(Timer timer) {
        if (timer.isPlayerTimer()) {
            Map<String, Timer> timers = playerTimers.get(timer.getPlayerId());

            if (timers == null || timers.get(timer.getKey()) != timer) {
                return;
            }

            timers.remove(timer.getKey());

            if (timers.isEmpty()) {
                playerTimers.remove(timer.getPlayerId());
            }

            return;
        }

        if (serverTimers.get(timer.getKey()) == timer) {
            serverTimers.remove(timer.getKey());
        }
    }

    private static long normalizeDelay(long delayTicks) {
        return Math.max(1L, delayTicks);
    }

    private static long normalizeExecuteTick(long executeTick) {
        return Math.max(currentTick + 1, executeTick);
    }

    private static long normalizeShiftTicks(long ticks) {
        return Math.max(0L, ticks);
    }

    private static String requireKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Timer key can't be null");
        }

        String normalized = key.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Timer key can't be empty");
        }

        return normalized;
    }

    private static UUID requirePlayerId(UUID playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("Player id can't be null");
        }

        return playerId;
    }
}
