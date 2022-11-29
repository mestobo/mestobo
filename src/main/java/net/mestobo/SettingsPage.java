package net.mestobo;

import com.google.inject.Inject;

import javafx.scene.control.MenuItem;
import net.mestobo.settings.SettingsManager;

public class SettingsPage implements MenuProvider {

	@Inject
	private SettingsManager settingsManager;

	@Override
	public void setupMenu(MestoboMenuBar menuBar) {
		MenuItem menuItem = new MenuItem(I18N.get("Settings"));
		menuBar.addMenu(I18N.get("Extras"), "settings", menuItem, e -> settingsManager.showSettings(false));
	}
}
