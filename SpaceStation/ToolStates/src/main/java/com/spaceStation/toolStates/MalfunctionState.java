package com.spaceStation.toolStates;

import com.spaceStation.toolStateInterface.ToolState;

public class MalfunctionState implements ToolState {
    @Override
    public ToolState Next(final boolean isRepaired) {
        return (isRepaired ? new ReadyState() : this);
    }
}
