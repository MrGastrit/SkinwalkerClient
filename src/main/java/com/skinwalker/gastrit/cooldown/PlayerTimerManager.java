package com.skinwalker.gastrit.cooldown;

import com.skinwalker.gastrit.core.TimerManager;

import java.util.UUID;

@Deprecated
public class PlayerTimerManager {
    public void clear(UUID playerId) {
        TimerManager.clearPlayer(playerId);
    }
}
