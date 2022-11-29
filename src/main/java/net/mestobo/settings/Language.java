package net.mestobo.settings;

import java.util.Locale;

import net.mestobo.I18N;

public enum Language {

	ENGLISH("English", Locale.ROOT),
	GERMAN("German", Locale.GERMAN);

	private String display;
	private Locale locale;

	private Language(String displayKey, Locale locale) {
		this.display = I18N.get(displayKey);
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		return display;
	}
}
