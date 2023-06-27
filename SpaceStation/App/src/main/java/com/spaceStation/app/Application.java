package com.spaceStation.app;

import com.spaceStation.appStateInterface.AppState;
import com.spaceStation.appStates.MainMenuState;
import com.spaceStation.fileFleetLoader.FileFleetLoader;
import com.spaceStation.fleet.FleetManager;

import java.util.Scanner;
import java.util.concurrent.Executors;

public class Application {
    private static final String CONFIG_FILE_ADDRESS = "./config.txt";
    private static Application m_instance = null;
    private Application() {m_instance = this;}
    public static Application INSTANCE() {
        if (m_instance == null) {
            m_instance = new Application();
        }
        return m_instance;
    }
    public void run() {
        System.out.println("Hello! welcome to the Space Station Robot Fleet App!\n");
        FleetManager fleetManager = FleetManager.INSTANCE();
        var fleetLoader = new FileFleetLoader();
        fleetLoader.getFleet(CONFIG_FILE_ADDRESS, fleetManager);
        var executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            AppState state = MainMenuState.INSTANCE();
            try (var sc = new Scanner(System.in)) {
                while (state != null) {
                    final int userInput = state.userInteraction(fleetManager, sc);
                    state = state.Next(userInput);
                }
            }
            executor.shutdownNow();
            System.out.println("Goodbye, and thanks for all the fish!\n ⋖')))))‑{");
        });
        executor.submit(() -> cosmicRays(fleetManager));
        executor.shutdown();
        executor.close();
    }
    private void cosmicRays(FleetManager fleetManager) {
        while (fleetManager != null) {
            try {
                Thread.sleep(15_000);
                fleetManager.simulateCosmicRays();
            }
            catch (InterruptedException e) {
                throw new RuntimeException("Simulating cosmic rays interrupted.");
            }
        }
    }
}
