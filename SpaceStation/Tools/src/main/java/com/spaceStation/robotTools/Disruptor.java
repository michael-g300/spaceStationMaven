package com.spaceStation.robotTools;

import com.spaceStation.toolInterface.Tool;
import com.spaceStation.toolStateInterface.ToolState;
import com.spaceStation.toolStates.MalfunctionState;
import com.spaceStation.toolStates.ReadyState;

import java.security.SecureRandom;

public class Disruptor implements Tool {
    private final String m_name;
    private ToolState m_state;
    public Disruptor() {
        m_name = "Disruptor";
        m_state = new ReadyState();
    }
    @Override
    public void use() {
        var random = new SecureRandom();
        var success = random.nextDouble(0,1);
        m_state = m_state.Next(success > 0.2);
    }

    @Override
    public boolean repair() {
        if (m_state.getClass().getSimpleName().equals("ReadyState")) {
            return true;
        }
        try {
            System.out.println("Repairing tool " + m_name);
            Thread.sleep(new SecureRandom().nextInt(1_000,5_000));
        }
        catch (InterruptedException e) {
            throw new RuntimeException("Tool repair interrupted");
        }
        var random = new SecureRandom();
        var success = random.nextInt(1,100);
        m_state = m_state.Next(success <= 90);
        return m_state.getClass().getSimpleName().equals("ReadyState");
    }
    @Override
    public String name() {
        return m_name;
    }

    @Override
    public void setState(ToolState state) {
        m_state = state;
    }

    @Override
    public boolean isBad() {
        return m_state instanceof MalfunctionState;
    }
}
