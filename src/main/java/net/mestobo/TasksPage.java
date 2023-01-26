package net.mestobo;

import com.google.inject.Inject;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/** TasksPage shows background tasks */
public class TasksPage extends MenuPage {
	
	@Inject
	private BackgroundTaskExecutor backgroundTaskExecutor;
	
	public TasksPage() {
		super(I18N.get("Tasks"));
	}

	@Override
	protected Node createPresentation() {
		TableView<Task<?>> table = new TableView<>();
		table.setItems(backgroundTaskExecutor.getTasks());
		
		TableColumn<Task<?>, String> titleColumn = new TableColumn<>(I18N.get("Title"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		table.getColumns().add(titleColumn);
		
		TableColumn<Task<?>, String> stateColumn = new TableColumn<>(I18N.get("Status"));
		stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
		table.getColumns().add(stateColumn);
		
		TableColumn<Task<?>, String> messageColumn = new TableColumn<>(I18N.get("Message"));
		messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		table.getColumns().add(messageColumn);

		return table;
	}
	
	@Override
	public String getMenuLabel() {
		return I18N.get("Extras");
	}
	
	@Override
	public String getMenuCategory() {
		return "settings";
	}

	@Override
	public String getMenuItemLabel() {
		return I18N.get("Tasks");
	}
}
