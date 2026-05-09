package com.skinwalker.gastrit.mappet;

import com.skinwalker.gastrit.api.TimerApi;
import com.skinwalker.gastrit.cooldown.PlayerTimerApi;
import mchorse.mappet.events.RegisterScriptVariablesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MappetScriptVariables {
    private static final BridgeApi BRIDGE = new BridgeApi();
    private static final TimerApi SERVER_TIMERS = BRIDGE.server();
    private static final PlayerTimerApi PLAYER_TIMERS = BRIDGE.players();

    @SubscribeEvent
    public void onRegister(RegisterScriptVariablesEvent event) {
        event.register("Bridge", BRIDGE);
        event.register("ServerTimers", SERVER_TIMERS);
        event.register("PlayerTimers", PLAYER_TIMERS);
    }
}
