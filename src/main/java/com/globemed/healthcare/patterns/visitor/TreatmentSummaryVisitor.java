package com.globemed.healthcare.patterns.visitor;

import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.model.BillingInfo;
import com.globemed.healthcare.model.PatientRecord;

import java.time.format.DateTimeFormatter;

public class TreatmentSummaryVisitor implements ReportVisitor {
    private StringBuilder reportContent = new StringBuilder();

    @Override
    public String visit(PatientRecord patientRecord) {
        reportContent.append("--- Patient Details ---\n");
        reportContent.append("ID: ").append(patientRecord.getPatientId()).append("\n");
        reportContent.append("Name: ").append(patientRecord.getFirstName()).append(" ").append(patientRecord.getLastName()).append("\n");
        reportContent.append("DOB: ").append(patientRecord.getDateOfBirth()).append("\n");
        reportContent.append("\n--- Medical History ---\n");
        reportContent.append(patientRecord.getMedicalHistory()).append("\n");
        reportContent.append("\n--- Treatment Plan ---\n");
        reportContent.append(patientRecord.getTreatmentPlan()).append("\n");
        return reportContent.toString();
    }

    @Override
    public String visit(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        reportContent.append("\n--- Appointment on ").append(appointment.getAppointmentDateTime().format(formatter)).append(" ---\n");
        reportContent.append("Doctor: ").append(appointment.getDoctorName()).append("\n");
        reportContent.append("Reason: ").append(appointment.getReason()).append("\n");
        reportContent.append("Status: ").append(appointment.getStatus()).append("\n");
        return reportContent.toString();
    }

    @Override
    public String visit(BillingInfo billingInfo) {

        return "";
    }

    @Override
    public String getReport() {
        return "********* TREATMENT SUMMARY REPORT *********\n" + reportContent.toString();
    }
}
