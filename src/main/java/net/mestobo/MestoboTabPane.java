package net.mestobo;

import com.google.inject.Singleton;
import com.panemu.tiwulfx.control.dock.DetachableTabPane;

import javafx.scene.input.KeyCombination;

@Singleton
public class MestoboTabPane extends  DetachableTabPane implements MenuProvider {

	public MestoboTabPane() {
		setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
	}
	
	@Override
	public void setupMenu(MestoboMenuBar menuBar) {
		menuBar.addMenu(
			I18N.get("Extras"), 
			"tabhandling", 
			createMenuItem(I18N.get("NewTab"), KeyCombination.keyCombination("Shortcut+T")), 
			e -> newTab()
		);
		menuBar.addMenu(
			I18N.get("Extras"), 
			"tabhandling", 
			createMenuItem(I18N.get("CloseTab"), KeyCombination.keyCombination("Shortcut+W")), 
			e -> closeTab()
		);
	}

	public void newTab() {
		addTab("        ", null);
	}	
	
	public void closeTab() {
		getTabs().remove(getSelectionModel().getSelectedItem());
	}
}
