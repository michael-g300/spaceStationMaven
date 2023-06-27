package com.spaceStation.fileFleetLoader;

import com.spaceStation.fleet.FleetManager;
import com.spaceStation.fleetLoaderInterface.FleetLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileFleetLoader implements FleetLoader {
    private static final String CATEGORY_SEPARATOR = ";";
    private static final int ROBOT_COMPONENTS = 3;
    public void getFleet(final String fileLocation, final FleetManager fleetManager) {
        var filePath = Paths.get(fileLocation);
        if (!Files.exists(filePath)) {
            System.out.println("Incorrect configuration file location.");
            return;
        }
        try {
            System.out.println("Loading initial robot fleet from file.");
            List<String> robotsInfo = Files.readAllLines(filePath);
            for (String robotInfo : robotsInfo) {
                extractRobotFromFile(robotInfo, fleetManager);
            }
        }
        catch (IOException e) {
            System.out.println("Unable to load configuration file");
            throw new IllegalArgumentException();
        }
    }

    private void extractRobotFromFile(final String robotInfo, final FleetManager fleetManager) {
        final String[] robotComponents = robotInfo.split(CATEGORY_SEPARATOR);
        if (robotComponents.length == ROBOT_COMPONENTS) {
            fleetManager.createRobot(robotComponents[0], robotComponents[1], robotComponents[2]);
        }
        else {
            System.out.println("Invalid robot info received from configuration file:");
        }
    }
}
