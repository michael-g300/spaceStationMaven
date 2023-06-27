package com.spaceStation.robots;

import com.spaceStation.robotCommands.RobotCommands;
import com.spaceStation.robotInterface.Robot;
import com.spaceStation.robotStateInterface.RobotState;
import com.spaceStation.robotStates.ActiveState;
import com.spaceStation.robotStates.FailingState;
import com.spaceStation.robotStates.RebootingState;
import com.spaceStation.robotTools.LaserCutter;
import com.spaceStation.robotTools.StaticBrush;
import com.spaceStation.toolInterface.Tool;
import com.spaceStation.toolStates.MalfunctionState;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Johnny5 implements Robot {
    private final String m_name;
    private final String m_callSign;
    private final List<Tool> m_tools;
    private RobotState m_state;
    private static final ReentrantLock lock = new ReentrantLock(false);
    public Johnny5(final String name, final String callSign) {
        m_name = name;
        m_callSign = callSign;
        m_tools = new ArrayList<>();
        m_tools.add(new LaserCutter());
        m_tools.add(new StaticBrush());
        m_state = new ActiveState();
    }
    @Override
    public void work() {
        try {
            m_state = m_state.Next(RobotCommands.START_WORK);
            Thread.sleep(new SecureRandom().nextInt(30_000, 180_000));
            int malfunctioningTools = 0;
            for (Tool tool : m_tools) {
                tool.use();
                if (tool.isBad()) {
                    ++malfunctioningTools;
                }
            }
            if (malfunctioningTools > 0) {
                this.setState(new FailingState());
            }
            else {
                this.setState(new ActiveState());
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(m_name + " robot of type " + this.getClass().toString() + " work has been interrupted. Work incomplete.");
        }
    }

    @Override
    public boolean SelfDiagnose() {
        this.setState(new RebootingState());
        int failedToolRepairs = 0;
        for (Tool tool : m_tools) {
            if (tool.isBad()) {
                failedToolRepairs += tool.repair() ? 0 : 1;
            }
        }
        if (failedToolRepairs == 0) {
            this.setState(new ActiveState());
        }
        return failedToolRepairs == 0;
    }
    @Override
    public String name() {
        return m_name;
    }

    @Override
    public String callSign() {
        return m_callSign;
    }
    @Override
    public RobotState state() {
        return m_state;
    }
    @Override
    public void setState(RobotState state) {
        lock.lock();
        m_state = state;
        lock.unlock();
    }
    public boolean hit() {
        var random = new SecureRandom();
        int hitTools = 0;
        for (Tool tool : m_tools) {
            final int hitProbability = random.nextInt(1, 10);
            if (hitProbability == 1) {
                tool.setState(new MalfunctionState());
                ++ hitTools;
            }
        }
        if (hitTools != 0) {
            this.setState(new FailingState());
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        var sbuilder = new StringBuilder();
        sbuilder.append("Call sign: ");
        sbuilder.append(m_callSign);

        sbuilder.append("; Model: ");
        sbuilder.append(this.getClass().getSimpleName());

        sbuilder.append("; Status: ");
        sbuilder.append(m_state.getClass().getSimpleName());

        sbuilder.append("; Tools: [");
        for (Tool tool : m_tools) {
            sbuilder.append(tool.name());
            sbuilder.append("(");
            sbuilder.append(tool.isBad() ? "Malfunctioning" : "Ready");
            sbuilder.append(")");
            sbuilder.append(m_tools.indexOf(tool) == m_tools.size() - 1 ? "]" : ", ");
        }
        return sbuilder.toString();
    }
}
