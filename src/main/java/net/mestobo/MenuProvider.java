package net.mestobo;

import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public interface MenuProvider {
	void setupMenu(MestoboMenuBar menuBar);
	
	default MenuItem createMenuItem(String label, KeyCombination accelerator) {
		MenuItem item = new MenuItem(label);
		item.setAccelerator(accelerator);
		return item;		
	}
}
