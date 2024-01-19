package com.example.ai_assignment3b;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Assignment3PartII extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Create a FXMLLoader to load the user interface layout from an FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(Assignment3PartII.class.getResource("Perceptron.fxml"));
        // Create a Scene using the loaded FXML layout, with dimensions 700x550
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        // Display the application window
        stage.show();
    }
    /**
     * the main method that launches the JavaFX application
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}