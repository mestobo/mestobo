package net.mestobo;

import com.google.inject.Inject;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MestoboApplication {

	private Scene scene;
    
    @Inject
    private MestoboMenuBar menuBar;
    
    @Inject
    private MestoboTabPane mestoboTabPane;
    
	public void start(Stage primaryStage) {
		primaryStage.setTitle(I18N.get("WindowTitle"));

        StackPane root = new StackPane();
        VBox vbox = GUIFactory.create(VBox.class, root, "mainbox");
                
        GUIFactory.prepare(mestoboTabPane, vbox, "tabpane");
        vbox.getChildren().addAll(menuBar, mestoboTabPane);
        
        root.getChildren().add(vbox);

		Screen primaryScreen = Screen.getPrimary();

		scene = new Scene(root, primaryScreen.getBounds().getWidth() / 2, primaryScreen.getBounds().getHeight() / 2);
		scene.getStylesheets().add("mestobo.css");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void showPage(Page page) {
		page.init();
		Tab tab = mestoboTabPane.getSelectionModel().getSelectedItem();
		if (tab == null) {
			tab = mestoboTabPane.addTab(page.getTitle(), page.getPresentation());
		} else {
			tab.setText(page.getTitle());
			tab.setContent(page.getPresentation());
		}
	}
}
