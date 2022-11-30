package net.mestobo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import net.mestobo.settings.SettingsManager;

@Singleton
public class StylesheetManager {

	private static final Logger LOGGER = Logger.getLogger(StylesheetManager.class.getName());

	private final ObservableList<String> styles;
	private final List<Scene> listeners = new ArrayList<>();

	@Inject
	public StylesheetManager(SettingsManager settingsManager) {
		styles = settingsManager.getSelectedThemes();
		styles.addListener(this::onThemesChanged);
	}

	/**
	 * @param scene Scene with set Window
	 * @throws NullPointerException if given Scene instance has no Window set
	 */
	public void addListener(Scene scene) {
		applyStyles(scene);
		listeners.add(scene);
		scene.getWindow().setOnHidden(this::removeListener);
		LOGGER.info("added listener: " + listeners.size());
	}

	private void removeListener(WindowEvent e) {
		Window window = (Window)e.getSource();
		listeners.remove(window.getScene());
		LOGGER.info("removed listener: " + listeners.size());
	}

	private void onThemesChanged(Change<? extends String> change) {
		for (Scene scene : listeners) {
			applyStyles(scene);
		}
	}

	private void applyStyles(Scene scene) {
		List<String> stylesheets = scene.getStylesheets();
		stylesheets.clear();
		stylesheets.addAll(styles);
	}
}
