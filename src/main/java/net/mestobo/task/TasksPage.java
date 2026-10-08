package net.mestobo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.mestobo.I18N;
import net.mestobo.MenuPage;

/** TasksPage shows background tasks */
public class TasksPage extends MenuPage {
	
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	@Inject
	private BackgroundTaskExecutor backgroundTaskExecutor;
	
	public TasksPage() {
		super(I18N.get("Tasks"));
	}

	@Override
	protected Node createPresentation() {
		TableView<Task<?>> table = new TableView<>();
		table.setItems(backgroundTaskExecutor.getTasks());
		
		TableColumn<Task<?>, LocalDateTime> startedColumn = new TableColumn<>(I18N.get("StartedAt"));
		startedColumn.setCellValueFactory(new PropertyValueFactory<>("started"));
		startedColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		startedColumn.setCellFactory(_ -> createTimestampCell());
		table.getColumns().add(startedColumn);
		
		TableColumn<Task<?>, LocalDateTime> modifiedColumn = new TableColumn<>(I18N.get("LastModified"));
		modifiedColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));
		modifiedColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		modifiedColumn.setCellFactory(_ -> createTimestampCell());
		table.getColumns().add(modifiedColumn);
		
		TableColumn<Task<?>, String> titleColumn = new TableColumn<>(I18N.get("Title"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		titleColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.33));
		table.getColumns().add(titleColumn);
		
		TableColumn<Task<?>, String> stateColumn = new TableColumn<>(I18N.get("Status"));
		stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
		stateColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		table.getColumns().add(stateColumn);
		
		TableColumn<Task<?>, String> messageColumn = new TableColumn<>(I18N.get("Message"));
		messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		messageColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.55));
		table.getColumns().add(messageColumn);

		return table;
	}
	
	private TableCell<Task<?>, LocalDateTime> createTimestampCell() {
		return new TableCell<Task<?>, LocalDateTime>() {
			@Override
		    protected void updateItem(LocalDateTime item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(DTF.format(item));
		        }
		    }
		};
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
