package net.mestobo;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

/** FxLauncher serves as bootstrap to launch the application that has been built by a DI framework. */
public class FxLauncher extends Application {

	private static MestoboApplication application;
	private static Injector injector;

	public static Injector getInjector() {
		return injector;
	}

	public static void start(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		injector = Guice.createInjector(new InterfaceMultibindModule() {
			@Override
			protected void configure() {
				multibind(MenuProvider.class).toAllImplementers();
			}
		});
		application = injector.getInstance(MestoboApplication.class);
		application.start(primaryStage);
		primaryStage.show();
	}
}
