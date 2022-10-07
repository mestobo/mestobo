package net.mestobo;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

/** MestoboMenuBar provides the main window menu bar for mestobo.
 * @author Robert Lichtenberger
 */
public class MestoboMenuBar extends MenuBar {

	private Menu hl7Menu;
	private Menu extrasMenu;
	private MenuItem hl7SendAdt;
	private MenuItem newTab;
	
	
	public MestoboMenuBar() {
		setupMenu();
	}


	private void setupMenu() {
		setupHl7Menu();
		setupExtrasMenu();
	}


	private void setupHl7Menu() {
		hl7Menu= new Menu(I18N.get("HL7")); 
		hl7SendAdt = new MenuItem(I18N.get("SendADT"));
		hl7SendAdt.setOnAction(e -> MestoboApplication.getInstance().showPage(new SendHL7Page()));
		hl7Menu.getItems().addAll(hl7SendAdt);
		getMenus().add(hl7Menu);		
	}
	
	private void setupExtrasMenu() {
		extrasMenu= new Menu(I18N.get("Extras")); 
		newTab = new MenuItem(I18N.get("NewTab"));
		newTab.setAccelerator(KeyCombination.keyCombination("Shortcut+T"));
		newTab.setOnAction(e -> MestoboApplication.getInstance().newTab());
		extrasMenu.getItems().addAll(newTab);
		getMenus().add(extrasMenu);		
	}


	
}
