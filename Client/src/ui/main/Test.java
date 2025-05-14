package ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class
Test extends Application {
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "mainViewUserFXML.fxml"));
        AnchorPane pane = fxmlLoader.load();
        Scene scene = new Scene(pane,500,300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
