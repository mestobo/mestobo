package net.mestobo;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * Thin wrapper to separate the main class from the JavaFX Application class.
 * This simplifies launching from IDEs
 */
public class Mestobo {
	public static void main(String[] args) throws SecurityException, IOException {
		LogManager.getLogManager().readConfiguration(Mestobo.class.getClassLoader().getResourceAsStream("log.properties"));

		FxLauncher.start(args);
	}
}
