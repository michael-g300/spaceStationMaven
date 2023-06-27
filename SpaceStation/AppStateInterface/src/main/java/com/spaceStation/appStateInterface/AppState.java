package com.spaceStation.appStateInterface;

import com.spaceStation.fleet.FleetManager;

import java.util.Scanner;

public interface AppState {
    AppState Next(int userChoice);
    int userInteraction(FleetManager fleetManager, Scanner scanner);
}
