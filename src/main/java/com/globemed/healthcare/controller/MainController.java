package com.globemed.healthcare.controller;

import com.globemed.healthcare.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button patientsButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button billingButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button staffButton;
    @FXML
    private Button logoutButton;
    @FXML
    private StackPane contentPane;

    private User currentUser;

    public void initData(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getRole() + " " + user.getUsername() + "!");
        setupPermissions();
    }

    private void setupPermissions() {
        if (currentUser == null) return;

        String role = currentUser.getRole();


        patientsButton.setVisible(false);
        appointmentsButton.setVisible(false);
        billingButton.setVisible(false);
        reportsButton.setVisible(false);
        staffButton.setVisible(false);


        switch (role) {
            case "Doctor":
                patientsButton.setVisible(true);
                appointmentsButton.setVisible(true);
                billingButton.setVisible(true);
                reportsButton.setVisible(true);
                staffButton.setVisible(true);
                break;
            case "Nurse":
                patientsButton.setVisible(true);
                appointmentsButton.setVisible(true);
                break;
            case "Pharmacist":
                billingButton.setVisible(true);
                break;
            default:

                break;
        }
    }

    @FXML
    private void showStaffView() {
        loadView("StaffView");
    }

    @FXML
    private void showPatientsView() {
        loadView("PatientView");
    }

    private void loadView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/globemed/healthcare/view/" + viewName + ".fxml"));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();

            contentPane.getChildren().setAll(new Label("Error: Could not load " + viewName));
        }
    }

    @FXML
    private void showAppointmentsView() {
        loadView("AppointmentView");
    }

    @FXML
    private void showBillingView() {
        loadView("BillingView");
    }

    @FXML
    private void showReportsView() {
        loadView("ReportView");
    }

    @FXML
    private void handleLogout() {
         try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/globemed/healthcare/view/LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setScene(scene);
            stage.setTitle("GlobeMed Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
