package net.mestobo;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/** MestoboMenuBar provides the main window menu bar for mestobo.
 * @author Robert Lichtenberger
 */
@Singleton
public class MestoboMenuBar extends MenuBar {

	private static Object CATEGORY_KEY = new Object();
	
	@Inject
	public MestoboMenuBar(Set<MenuProvider> menuProviders) {
		// To get a defined menu order, we predefine the main menu entries
		getOrCreateMenu(I18N.get("HL7"));
		getOrCreateMenu(I18N.get("Extras"));

		for (MenuProvider provider : menuProviders) {
			provider.setupMenu(this);
		}			
	}
	
	public void addMenu(String menuLabel, String category, MenuItem menuItem, EventHandler<ActionEvent> eventHandler) {
		Menu menu = getOrCreateMenu(menuLabel);
		menuItem.getProperties().put(CATEGORY_KEY, category);
		menuItem.setOnAction(eventHandler);
		menu.getItems().add(menuItem);
	}


	private Menu getOrCreateMenu(String menuLabel) {
		Menu menu = getMenus().stream()
			.filter(m -> m.getText().equals(menuLabel))
			.findFirst()
			.orElse(null);
		if (menu == null) {
			menu = new Menu(menuLabel);
			getMenus().add(menu);
		}
		return menu;
	}
}
