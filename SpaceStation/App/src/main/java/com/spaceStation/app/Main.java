package com.spaceStation.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String CONFIG_FILE_ADDRESS = "./config.txt";
    public static void main(String[] args) {
        //createConfigFile();
        var app = Application.INSTANCE();
        app.run();
    }
    private static void createConfigFile() {
        final Path filePath = Paths.get(CONFIG_FILE_ADDRESS);
        if (Files.exists(filePath)) {
            System.out.println("The file you are trying to create already exists.");
            return;
        }
        try {
            Files.createFile(filePath);
            var sbuilder = new StringBuilder();
            sbuilder.append("robot1;ADF45H6K;HAL9000\n");
            sbuilder.append("robot2;G6JFD44Y;Johnny5\n");
            sbuilder.append("robot3;BGF54RR;Maschinenmensch\n");
            sbuilder.append("robot4;LP09U7IG;Tachikomas");
            Files.writeString(filePath, sbuilder);
        }
        catch (IOException e) {
            System.out.println("Configuration file creation failed.");
            throw new IllegalArgumentException();
        }
    }
}