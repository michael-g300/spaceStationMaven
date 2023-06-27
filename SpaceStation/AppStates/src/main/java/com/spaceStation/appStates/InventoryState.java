package com.spaceStation.appStates;

import com.spaceStation.appStateInterface.AppState;
import com.spaceStation.fleet.FleetManager;

import java.util.Scanner;

public class InventoryState implements AppState {
    private static InventoryState m_instance = null;
    private InventoryState() {m_instance = this;};
    public static final InventoryState INSTANCE() {
        if (m_instance == null) {
            m_instance = new InventoryState();
        }
        return m_instance;
    }
    @Override
    public AppState Next(int userChoice) {
        return MainMenuState.INSTANCE();
    }

    @Override
    public int userInteraction(FleetManager fleetManager, Scanner scanner) {
        System.out.println("Your fleet:");
        System.out.println(fleetManager.ListFleet());
        return 1;
    }
}
