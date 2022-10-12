package net.mestobo;

import java.lang.reflect.InvocationTargetException;

import com.google.inject.Inject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public abstract class MenuPage extends Page implements MenuProvider {

	@Inject
	private MestoboApplication mestoboApp;
	
	public MenuPage(String title) {
		super(title);	
	}
	
	abstract public String getMenuLabel();
	abstract public String getMenuCategory();
	abstract public String getMenuItemLabel();

	@Override
	public void setupMenu(MestoboMenuBar menuBar) {
		MenuItem menuItem = new MenuItem(getMenuItemLabel());
		menuBar.addMenu(getMenuLabel(), getMenuCategory(), menuItem, getEventHandler());
	}
	
	private EventHandler<ActionEvent> getEventHandler()  {
		return e -> {
			try {
				mestoboApp.showPage(getClass().getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				throw new RuntimeException("Could not open page", e1);
			}
		};
	}
	


}
