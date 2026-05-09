package com.skinwalker.gastrit.core;

import com.skinwalker.gastrit.core.callback.ITimerCallback;

import java.util.UUID;

public class Timer {
    private final String key;
    private final TimerScope scope;
    private final UUID playerId;
    private final long createdTick;
    private final long executeTick;
    private final ITimerCallback callback;

    public Timer(String key, TimerScope scope, UUID playerId, long createdTick, long executeTick, ITimerCallback callback) {
        this.key = key;
        this.scope = scope;
        this.playerId = playerId;
        this.createdTick = createdTick;
        this.executeTick = executeTick;
        this.callback = callback;
    }

    public String getKey() {
        return this.key;
    }

    public String getScope() {
        return this.scope.name().toLowerCase();
    }

    public boolean isPlayerTimer() {
        return this.scope == TimerScope.PLAYER;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public long getCreatedTick() {
        return this.createdTick;
    }

    public long getExecuteTick() {
        return this.executeTick;
    }

    public boolean hasCallback() {
        return this.callback != null;
    }

    ITimerCallback getCallback() {
        return this.callback;
    }
}
