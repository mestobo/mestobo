package net.mestobo;

import com.panemu.tiwulfx.control.dock.DetachableTabPane;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

public class MestoboApplication extends Application {

	private static MestoboApplication instance;

	private Scene scene;
    private DetachableTabPane tabPane;
	
	public static void start(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		MestoboApplication.instance = this;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(I18N.get("WindowTitle"));

        StackPane root = new StackPane();
        VBox vbox = GUIFactory.create(VBox.class, root, "mainbox");
        vbox.getStyleClass().add(JMetroStyleClass.BACKGROUND);					
                
        createTabPane(vbox);
        vbox.getChildren().addAll(new MestoboMenuBar(), tabPane);
        
        root.getChildren().add(vbox);

		Screen primaryScreen = Screen.getPrimary();

		scene = new Scene(root, primaryScreen.getBounds().getWidth() / 2, primaryScreen.getBounds().getHeight() / 2);
		scene.getStylesheets().add("mestobo.css");
		JMetro jMetro = new JMetro(Style.LIGHT);
		jMetro.setScene(scene);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void createTabPane(Node parent) {
		tabPane = GUIFactory.create(DetachableTabPane.class, parent, "tabpane");
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		tabPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);							
	}

	public void showPage(Page page) {
		page.init();
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		if (tab == null) {
			tab = tabPane.addTab(page.getTitle(), page.getPresentation());
		} else {
			tab.setText(page.getTitle());
			tab.setContent(page.getPresentation());
		}
	}
	
	public void newTab() {
		tabPane.addTab("        ", null);
	}
	
	public static MestoboApplication getInstance() {
		return instance;
	}
}
