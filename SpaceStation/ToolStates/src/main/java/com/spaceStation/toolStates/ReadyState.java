package com.spaceStation.toolStates;

import com.spaceStation.toolStateInterface.ToolState;

public class ReadyState implements ToolState {
    @Override
    public ToolState Next(final boolean isStillWorking) {
        return (isStillWorking ? this : new MalfunctionState());
    }
}
