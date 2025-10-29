package com.globemed.healthcare.controller;

import com.globemed.healthcare.dao.AppointmentDao;
import com.globemed.healthcare.dao.PatientDao;
import com.globemed.healthcare.model.*;
import com.globemed.healthcare.patterns.visitor.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class ReportController {

    @FXML private ComboBox<PatientRecord> patientComboBox;
    @FXML private ComboBox<String> reportTypeComboBox;
    @FXML private Button generateButton;
    @FXML private TextArea reportArea;

    private final PatientDao patientDao = new PatientDao();
    private final AppointmentDao appointmentDao = new AppointmentDao();

    @FXML
    public void initialize() {

        patientComboBox.setItems(FXCollections.observableArrayList(patientDao.listAll()));
        patientComboBox.setConverter(new StringConverter<PatientRecord>() {
            @Override public String toString(PatientRecord patient) { return patient == null ? "" : patient.getFirstName() + " " + patient.getLastName(); }
            @Override public PatientRecord fromString(String string) { return null; }
        });

        reportTypeComboBox.setItems(FXCollections.observableArrayList("Treatment Summary", "Financial Report"));
    }

    @FXML
    private void handleGenerateReport() {
        PatientRecord patient = patientComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();

        if (patient == null || reportType == null) {
            reportArea.setText("Please select a patient and a report type.");
            return;
        }


        ReportVisitor visitor;
        if (reportType.equals("Treatment Summary")) {
            visitor = new TreatmentSummaryVisitor();
        } else {
            visitor = new FinancialReportVisitor();
        }


        List<Reportable> reportableItems = new ArrayList<>();
        reportableItems.add(patient); // Add the patient record itself


        appointmentDao.listAll().stream()
                .filter(a -> a.getPatientId().equals(patient.getPatientId()))
                .forEach(reportableItems::add);


        Invoice dummyInvoice = new Invoice("INV-DUMMY-01", patient);
        dummyInvoice.addItem(new BillItem("Consultation", 150.00));
        dummyInvoice.addItem(new BillItem("X-Ray", 75.50));
        reportableItems.add(new BillingInfo(dummyInvoice));



        for (Reportable item : reportableItems) {
            item.accept(visitor);
        }


        reportArea.setText(visitor.getReport());
    }
}
