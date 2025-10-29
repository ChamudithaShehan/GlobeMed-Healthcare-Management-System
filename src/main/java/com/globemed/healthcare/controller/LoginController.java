package com.globemed.healthcare.controller;

import com.globemed.healthcare.dao.UserDao;
import com.globemed.healthcare.model.User;
import com.globemed.healthcare.patterns.security.AuthenticationService;
import com.globemed.healthcare.patterns.security.BasicAuthenticationService;
import com.globemed.healthcare.patterns.security.EncryptionDecorator;
import com.globemed.healthcare.patterns.security.LoggingDecorator;
import com.globemed.healthcare.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private AuthenticationService authService;
    private final UserDao userDao = new UserDao();

    @FXML
    public void initialize() {

        AuthenticationService basicAuth = new BasicAuthenticationService();

        this.authService = new LoggingDecorator(new EncryptionDecorator(basicAuth));
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        boolean success = authService.login(username, password);

        if (success) {
            errorLabel.setText("Login Successful!");
            errorLabel.setStyle("-fx-text-fill: green;");

            User user = userDao.findByUsername(username).orElse(null);
            Session.setCurrentUser(user);
            loadMainApplication(user);
        } else {
            errorLabel.setText("Invalid username or password.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void loadMainApplication(User user) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/globemed/healthcare/view/MainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);


            MainController mainController = fxmlLoader.getController();
            mainController.initData(user);


            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("GlobeMed Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load the main application.");
        }
    }
}
