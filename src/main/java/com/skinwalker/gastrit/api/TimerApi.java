package com.skinwalker.gastrit.api;

import com.skinwalker.gastrit.core.Timer;
import com.skinwalker.gastrit.core.TimerManager;
import com.skinwalker.gastrit.mappet.MappetScriptTimerCallback;

public class TimerApi {
    public long now() {
        return TimerManager.getCurrentTick();
    }

    public long start(String key, long delayTicks) {
        return TimerManager.setServer(key, delayTicks);
    }

    public long startAt(String key, long executeTick) {
        return TimerManager.setServerAt(key, executeTick);
    }

    public long startCallback(String key, long delayTicks, String script) {
        return startCallback(key, delayTicks, script, "main");
    }

    public long startCallback(String key, long delayTicks, String script, String function) {
        return TimerManager.setServer(key, delayTicks, new MappetScriptTimerCallback(script, function));
    }

    public long startCallbackAt(String key, long executeTick, String script, String function) {
        return TimerManager.setServerAt(key, executeTick, new MappetScriptTimerCallback(script, function));
    }

    public boolean has(String key) {
        return TimerManager.hasServer(key);
    }

    public boolean ready(String key) {
        return TimerManager.isServerReady(key);
    }

    public long remaining(String key) {
        return TimerManager.getServerRemaining(key);
    }

    public long expireTick(String key) {
        return TimerManager.getServerExpireTick(key);
    }

    public Timer get(String key) {
        return TimerManager.getServerTimer(key);
    }

    public long add(String key, long ticks) {
        return TimerManager.addServerTicks(key, ticks);
    }

    public long reduce(String key, long ticks) {
        return TimerManager.reduceServerTicks(key, ticks);
    }

    public void cancel(String key) {
        TimerManager.cancelServer(key);
    }
}
