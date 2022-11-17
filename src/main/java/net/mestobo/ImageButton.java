package net.mestobo;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Button;

/** ImageButton allows to easily set an Iconli - icon to a button. */
public class ImageButton extends Button {
	public ImageButton withIcon(String iconPath) {
		setGraphic(new FontIcon(iconPath));
		return this;
	}
}
