package net.mestobo;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

// TODO :: remove me, if WorkbenchFX is used, we will not need this
/** MestoboMenuBar provides the main window menu bar for mestobo.
 * @author Robert Lichtenberger
 */
public class MestoboMenuBar extends MenuBar {

	private Menu hl7Menu;
	private MenuItem hl7SendAdt;
	
	
	public MestoboMenuBar() {
		setupMenu();
	}


	private void setupMenu() {
		setupHl7Menu();
	}


	private void setupHl7Menu() {
		hl7Menu= new Menu(I18N.get("HL7")); 
		hl7SendAdt = new MenuItem(I18N.get("SendADT"));
		hl7Menu.getItems().addAll(hl7SendAdt);
		getMenus().add(hl7Menu);		
	}
}
