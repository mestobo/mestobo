package net.mestobo;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

/** FxLauncher serves as bootstrap to launch the application that has been built by a DI framework. */
public class FxLauncher extends Application {
	
	public static MestoboApplication application;

	public static void start(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Injector injector = Guice.createInjector(new AbstractModule() {
		});
		application = injector.getInstance(MestoboApplication.class);
		application.start(primaryStage);
		primaryStage.show();
	}
}
