package net.mestobo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MestoboApplication extends Application {
    public static void start(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button button = new Button();
        button.setText("Say 'Hello World'");
        button.setOnAction((ActionEvent event) -> System.out.println("Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(button);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
