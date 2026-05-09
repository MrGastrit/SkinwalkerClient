package com.skinwalker.gastrit.mappet;

import com.skinwalker.gastrit.core.Timer;
import com.skinwalker.gastrit.core.callback.ITimerCallback;

public class MappetScriptTimerCallback implements ITimerCallback {
    private final String script;
    private final String function;

    public MappetScriptTimerCallback(String script, String function) {
        if (script == null || script.trim().isEmpty()) {
            throw new IllegalArgumentException("Script name can't be empty");
        }

        if (function == null || function.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name can't be empty");
        }

        this.script = script.trim();
        this.function = function.trim();
    }

    @Override
    public void execute(Timer timer) {
        MappetBridge.executeForTimer(timer, this.script, this.function);
    }
}
