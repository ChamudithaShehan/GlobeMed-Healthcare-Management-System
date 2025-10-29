package com.globemed.healthcare.model;

import com.globemed.healthcare.patterns.visitor.ReportVisitor;
import com.globemed.healthcare.patterns.visitor.Reportable;

import java.time.LocalDate;

public class PatientRecord implements Reportable {
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String medicalHistory;
    private String treatmentPlan;

    // Constructor for new patient
    public PatientRecord(String patientId, String firstName, String lastName, LocalDate dateOfBirth, String gender, String address, String phoneNumber, String email) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.medicalHistory = "";
        this.treatmentPlan = "";
    }


    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getTreatmentPlan() { return treatmentPlan; }
    public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }


    public PatientRecord copy() {
        PatientRecord newRecord = new PatientRecord(this.patientId, this.firstName, this.lastName, this.dateOfBirth, this.gender, this.address, this.phoneNumber, this.email);
        newRecord.setMedicalHistory(this.medicalHistory);
        newRecord.setTreatmentPlan(this.treatmentPlan);
        return newRecord;
    }

    @Override
    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}
