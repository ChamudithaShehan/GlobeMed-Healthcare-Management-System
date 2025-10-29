package com.globemed.healthcare.model;

import com.globemed.healthcare.patterns.visitor.ReportVisitor;
import com.globemed.healthcare.patterns.visitor.Reportable;

import java.time.LocalDateTime;

public class Appointment implements Reportable {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String reason;

    public Appointment(String appointmentId, String patientId, String doctorId, LocalDateTime appointmentDateTime, String reason) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = "Scheduled";
    }


    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }


    public String getPatientName() {
        return patientId;
    }

    public String getDoctorName() {
        return "Dr. " + doctorId;
    }

    @Override
    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}
