package net.mestobo.settings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.google.common.io.Files;
import com.google.inject.Singleton;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.mestobo.I18N;

@Singleton
public class SettingsManager {

	private static final Logger LOGGER = Logger.getLogger(SettingsManager.class.getName());
	private static final Theme THEME_DEFAULT = new Theme("Default Light", "mestobo.css");

	private PreferencesFx preferences;
	private ObservableList<String> selectedThemes = FXCollections.observableArrayList(THEME_DEFAULT.getStylesheet());
	private ObjectProperty<Locale> selectedlanguage = new SimpleObjectProperty<>(Locale.GERMAN);

	public SettingsManager() {
		preferences = createPreferences();
		setStylesInternal(selectedThemes);
	}

	public ReadOnlyObjectProperty<Locale> getSelectedLanguage() {
		return selectedlanguage;
	}

	public ObservableList<String> getSelectedThemes() {
		return FXCollections.unmodifiableObservableList(selectedThemes);
	}

	public void showSettings(boolean modal) {
		preferences.show(modal);
	}

	private PreferencesFx createPreferences() {
		LOGGER.info("Loading settings");

		ObjectProperty<Language> languageSelection = new SimpleObjectProperty<>(Language.GERMAN);
		ObjectProperty<Theme> themeSelection = new SimpleObjectProperty<>(THEME_DEFAULT);
		languageSelection.addListener((obs, oldv, newv) -> selectedlanguage.setValue(newv.getLocale()));
		themeSelection.addListener(this::onThemeSelectionChanged);

		return PreferencesFx.of(getClass(),
            Category.of("General",
                Group.of(I18N.get("InternationalSettings"),
                    Setting.of(I18N.get("Language"), createLanguageOptions(), languageSelection)),
                Group.of(I18N.get("Display"),
                    Setting.of(I18N.get("Theme"), createThemeOptions(), themeSelection))))
            .persistWindowState(false)
            .saveSettings(true)
            .debugHistoryMode(true)
            .buttonsVisibility(true)
            .instantPersistent(false);
	}

	private ListProperty<Language> createLanguageOptions() {
		return new SimpleListProperty<>(FXCollections.observableList(List.of(Language.ENGLISH, Language.GERMAN)));
	}

	private ListProperty<Theme> createThemeOptions() {
		List<Theme> themes = new ArrayList<>();
		themes.add(THEME_DEFAULT);

		try {
			URL resource = getClass().getClassLoader().getResource("themes");
			for (File theme : Paths.get(resource.toURI()).toFile()
				.listFiles(path -> StringUtils.endsWithIgnoreCase(path.getName(), ".css"))) {
				themes.add(createTheme(theme));
			}
		} catch (MalformedURLException | URISyntaxException ex) {
			LOGGER.log(Level.WARNING, "Error loading stylesheets", ex);
		}

		return new SimpleListProperty<>(FXCollections.observableList(themes));
	}

	private Theme createTheme(File stylesheet) throws MalformedURLException {
		String name = Arrays.stream(Files.getNameWithoutExtension(stylesheet.getName())
				.split("[\\W_]+"))
				.map(s -> StringUtils.isBlank(s) ? "" : Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
		File encoded = new File(stylesheet.toURI().toURL().toString());
		return new Theme(name, "themes/" + encoded.getName());
	}

	private void onThemeSelectionChanged(ObservableValue<? extends Theme> observable, Theme oldValue, Theme newValue) {
		List<String> newValues = Stream.of(THEME_DEFAULT.getStylesheet(), newValue.getStylesheet()).distinct().toList();
		selectedThemes.setAll(newValues);
		setStylesInternal(newValues);
	}

	private void setStylesInternal(List<String> newValues) {
		if (preferences != null) {
			preferences.getStylesheets().clear();
			preferences.getStylesheets().addAll(newValues);
		}
	}
}
