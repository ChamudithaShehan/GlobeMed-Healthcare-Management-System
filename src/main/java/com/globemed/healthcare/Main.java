package com.globemed.healthcare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/com/globemed/healthcare/view/LoginView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("GlobeMed Healthcare Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
