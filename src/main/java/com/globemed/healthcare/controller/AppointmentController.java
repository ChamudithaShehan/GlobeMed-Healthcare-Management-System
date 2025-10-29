package com.globemed.healthcare.controller;

import com.globemed.healthcare.dao.AppointmentDao;
import com.globemed.healthcare.dao.PatientDao;
import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.model.User;
import com.globemed.healthcare.patterns.mediator.ConcreteSchedulerMediator;
import com.globemed.healthcare.patterns.mediator.SchedulerMediator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class AppointmentController {


	@FXML private TableView<Appointment> appointmentsTable;
	@FXML private TableColumn<Appointment, String> appIdColumn;
	@FXML private TableColumn<Appointment, String> patientColumn;
	@FXML private TableColumn<Appointment, String> doctorColumn;
	@FXML private TableColumn<Appointment, LocalDateTime> dateTimeColumn;
	@FXML private TableColumn<Appointment, String> statusColumn;
	@FXML private TableColumn<Appointment, String> reasonColumn;
	@FXML private ComboBox<PatientRecord> patientComboBox;
	@FXML private ComboBox<User> doctorComboBox;
	@FXML private DatePicker datePicker;
	@FXML private ComboBox<LocalTime> timeComboBox;
	@FXML private TextField reasonField;
	@FXML private Button bookButton;
	@FXML private Button cancelButton;

	private SchedulerMediator scheduler;
	private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
	private final AppointmentDao appointmentDao = new AppointmentDao();
	private final PatientDao patientDao = new PatientDao();
	private Map<String, String> patientIdToName = new HashMap<>();

	@FXML
	public void initialize() {
		this.scheduler = new ConcreteSchedulerMediator();


		setupTableColumns();
		setupComboBoxes();
		populateTimeSlots();

		loadAppointments();
	}

	private void setupTableColumns() {
		appIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
		patientColumn.setCellValueFactory(cell -> {
			Appointment a = cell.getValue();
			String name = patientIdToName.getOrDefault(a.getPatientId(), a.getPatientId());
			return new javafx.beans.property.SimpleStringProperty(name);
		});
		doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
	}

	private void setupComboBoxes() {

		patientIdToName.clear();
		for (PatientRecord p : patientDao.listAll()) {
			patientIdToName.put(p.getPatientId(), p.getFirstName() + " " + p.getLastName());
		}

		patientComboBox.setItems(FXCollections.observableArrayList(scheduler.getRegisteredPatients()));
		patientComboBox.setConverter(new StringConverter<PatientRecord>() {
			@Override public String toString(PatientRecord patient) { return patient == null ? "" : patient.getFirstName() + " " + patient.getLastName(); }
			@Override public PatientRecord fromString(String string) { return null; }
		});


		doctorComboBox.setItems(FXCollections.observableArrayList(scheduler.getAvailableDoctors()));
		doctorComboBox.setConverter(new StringConverter<User>() {
			@Override public String toString(User doctor) { return doctor == null ? "" : "Dr. " + doctor.getUsername(); }
			@Override public User fromString(String string) { return null; }
		});
	}

	private void populateTimeSlots() {

		ObservableList<LocalTime> timeSlots = FXCollections.observableArrayList();
		IntStream.range(9, 17).forEach(hour -> timeSlots.add(LocalTime.of(hour, 0)));
		timeComboBox.setItems(timeSlots);
	}

	private void loadAppointments() {
		appointmentList.setAll(appointmentDao.listAll());
		appointmentsTable.setItems(appointmentList);
	}


	@FXML
	private void handleBookAppointment() {
		PatientRecord patient = patientComboBox.getValue();
		User doctor = doctorComboBox.getValue();
		LocalDate date = datePicker.getValue();
		LocalTime time = timeComboBox.getValue();
		String reason = reasonField.getText();

		if (patient == null || doctor == null || date == null || time == null || reason.isEmpty()) {
			showAlert("Validation Error", "All fields are required to book an appointment.");
			return;
		}

		LocalDateTime dateTime = LocalDateTime.of(date, time);
		boolean success = scheduler.bookAppointment(patient.getPatientId(), doctor.getUsername(), dateTime, reason);

		if (success) {
			showAlert("Success", "Appointment booked successfully!");
			loadAppointments(); // Refresh the table
			clearForm();
		} else {
			showAlert("Booking Failed", "The selected time slot is not available for Dr. " + doctor.getUsername());
		}
	}

	@FXML
	private void handleCancelAppointment() {
		Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
		if (selectedAppointment == null) {
			showAlert("Error", "Please select an appointment to cancel.");
			return;
		}

		scheduler.cancelAppointment(selectedAppointment.getAppointmentId());
		showAlert("Success", "Appointment has been cancelled.");
		loadAppointments(); // Refresh the table to show the "Cancelled" status
	}

	private void clearForm() {
		patientComboBox.getSelectionModel().clearSelection();
		doctorComboBox.getSelectionModel().clearSelection();
		datePicker.setValue(null);
		timeComboBox.getSelectionModel().clearSelection();
		reasonField.clear();
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
