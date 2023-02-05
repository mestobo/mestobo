package net.mestobo;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.ToggleButton;

/** ImageButton allows to easily set an Iconli - icon to a button. */
public class ImageToggleButton extends ToggleButton {
	public ImageToggleButton withIcon(String iconPath) {
		setGraphic(new FontIcon(iconPath));
		return this;
	}
}
