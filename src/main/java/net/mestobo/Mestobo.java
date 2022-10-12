package net.mestobo;

/**
 * Thin wrapper to separate the main class from the JavaFX Application class.
 * This simplifies launching from IDEs
 */
public class Mestobo {
	public static void main(String[] args) {
		FxLauncher.start(args);
	}
}
