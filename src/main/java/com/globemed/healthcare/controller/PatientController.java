package com.globemed.healthcare.controller;

import com.globemed.healthcare.dao.PatientDao;
import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.patterns.memento.PatientRecordCaretaker;
import com.globemed.healthcare.patterns.memento.PatientRecordMemento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.UUID;

public class PatientController {


	@FXML private TextField searchField;
	@FXML private TableView<PatientRecord> patientsTable;
	@FXML private TableColumn<PatientRecord, String> idColumn;
	@FXML private TableColumn<PatientRecord, String> firstNameColumn;
	@FXML private TableColumn<PatientRecord, String> lastNameColumn;
	@FXML private TableColumn<PatientRecord, String> dobColumn;
	@FXML private TableColumn<PatientRecord, String> genderColumn;
	@FXML private TableColumn<PatientRecord, String> phoneColumn;
	@FXML private TableColumn<PatientRecord, String> emailColumn;
	@FXML private TableColumn<PatientRecord, String> addressColumn;
	@FXML private GridPane detailsPane;
	@FXML private TextField patientIdField;
	@FXML private TextField firstNameField;
	@FXML private TextField lastNameField;
	@FXML private DatePicker dobPicker;
	@FXML private ComboBox<String> genderComboBox;
	@FXML private TextField addressField;
	@FXML private TextField phoneField;
	@FXML private TextField emailField;
	@FXML private TextArea medicalHistoryArea;
	@FXML private TextArea treatmentPlanArea;
	@FXML private Button newButton;
	@FXML private Button saveButton;
	@FXML private Button deleteButton;
	@FXML private Button undoButton;

	private final ObservableList<PatientRecord> patientList = FXCollections.observableArrayList();
	private final PatientRecordCaretaker caretaker = new PatientRecordCaretaker();
	private final PatientDao patientDao = new PatientDao();
	private PatientRecord currentPatient;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

		genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

		loadPatients();
		setupTableSelectionListener();
		setupSearchFilter();


		detailsPane.setDisable(true);
	}

	private void loadPatients() {
		patientList.setAll(patientDao.listAll());
		patientsTable.setItems(patientList);
	}

	private void setupTableSelectionListener() {
		patientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				currentPatient = newSelection;
				displayPatientDetails(currentPatient);

				caretaker.saveState(new PatientRecordMemento(currentPatient));
				detailsPane.setDisable(false);
			} else {
				clearForm();
				detailsPane.setDisable(true);
			}
		});
	}

	private void setupSearchFilter() {
		FilteredList<PatientRecord> filteredData = new FilteredList<>(patientList, p -> true);
		searchField.textProperty().addListener((obs, oldVal, newVal) -> {
			filteredData.setPredicate(patient -> {
				if (newVal == null || newVal.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newVal.toLowerCase();
				if (patient.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (patient.getLastName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (patient.getPatientId().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
		patientsTable.setItems(filteredData);
	}

	private void displayPatientDetails(PatientRecord patient) {
		patientIdField.setText(patient.getPatientId());
		firstNameField.setText(patient.getFirstName());
		lastNameField.setText(patient.getLastName());
		dobPicker.setValue(patient.getDateOfBirth());
		genderComboBox.setValue(patient.getGender());
		addressField.setText(patient.getAddress());
		phoneField.setText(patient.getPhoneNumber());
		emailField.setText(patient.getEmail());
		medicalHistoryArea.setText(patient.getMedicalHistory());
		treatmentPlanArea.setText(patient.getTreatmentPlan());
	}

	private void clearForm() {
		patientIdField.clear();
		firstNameField.clear();
		lastNameField.clear();
		dobPicker.setValue(null);
		genderComboBox.getSelectionModel().clearSelection();
		addressField.clear();
		phoneField.clear();
		emailField.clear();
		medicalHistoryArea.clear();
		treatmentPlanArea.clear();
		currentPatient = null;
	}


	@FXML
	private void handleNew() {
		patientsTable.getSelectionModel().clearSelection();
		clearForm();
		detailsPane.setDisable(false);
		patientIdField.setText("P" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
		firstNameField.requestFocus();
	}

	@FXML
	private void handleSave() {
		if (patientIdField.getText() == null || patientIdField.getText().trim().isEmpty()) {
			return;
		}
		boolean isNewPatient = currentPatient == null || !currentPatient.getPatientId().equals(patientIdField.getText());
		PatientRecord recordToSave = new PatientRecord(
				patientIdField.getText(),
				firstNameField.getText(),
				lastNameField.getText(),
				dobPicker.getValue(),
				genderComboBox.getValue(),
				addressField.getText(),
				phoneField.getText(),
				emailField.getText()
		);
		recordToSave.setMedicalHistory(medicalHistoryArea.getText());
		recordToSave.setTreatmentPlan(treatmentPlanArea.getText());
		patientDao.upsert(recordToSave);
		if (isNewPatient) {
			patientList.add(recordToSave);
		} else {
			int index = patientList.indexOf(currentPatient);
			if (index >= 0) {
				patientList.set(index, recordToSave);
			}
		}
		patientsTable.getSelectionModel().select(recordToSave);
	}

	@FXML
	private void handleDelete() {
		if (currentPatient != null) {
			patientDao.deleteById(currentPatient.getPatientId());
			patientList.remove(currentPatient);
			clearForm();
		}
	}

	@FXML
	private void handleUndo() {
		PatientRecordMemento memento = caretaker.undo();
		if (memento != null) {
			currentPatient = memento.getSavedState();
			displayPatientDetails(currentPatient);
		} else {
			System.out.println("No more states to undo.");
		}
	}
}
