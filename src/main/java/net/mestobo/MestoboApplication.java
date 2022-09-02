package net.mestobo;

import com.dlsc.workbenchfx.Workbench;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MestoboApplication extends Application {

	public static void start(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(I18N.get("WindowTitle"));

		Workbench workbench = Workbench.builder(new SendADTModule(), new SendADTModule()).modulesPerPage(6).build();

		Screen primaryScreen = Screen.getPrimary();

		primaryStage.setScene(new Scene(workbench, primaryScreen.getBounds().getWidth() / 2,
				primaryScreen.getBounds().getHeight() / 2));
		primaryStage.show();
	}
}
