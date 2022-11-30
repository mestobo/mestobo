package net.mestobo;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.panemu.tiwulfx.control.dock.DetachableTabPane;
import com.panemu.tiwulfx.control.dock.DetachableTabPaneFactory;
import com.panemu.tiwulfx.control.dock.TabStageFactory;

import javafx.scene.input.KeyCombination;

@Singleton
public class MestoboTabPane extends DetachableTabPane implements MenuProvider {

	@Inject
	private StylesheetManager stylesheetManager;

	public MestoboTabPane() {
		setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		setStageFactory(tabStageFactory);
		setDetachableTabPaneFactory(new DetachableTabPaneFactory() {
			@Override
			protected void init(DetachableTabPane newTabPane) {
				newTabPane.setStageFactory(tabStageFactory);
			}
		});
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

	private final TabStageFactory tabStageFactory = (tabPane, tab) -> {
		TabStage stage = new TabStage(tabPane, tab);
		stylesheetManager.addListener(stage.getScene());
		return stage;
	};
}
