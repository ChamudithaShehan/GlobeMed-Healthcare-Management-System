package com.globemed.healthcare.patterns.visitor;

import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.model.BillingInfo; // This will be a new class


public interface ReportVisitor {
    String visit(PatientRecord patientRecord);
    String visit(Appointment appointment);
    String visit(BillingInfo billingInfo);

    String getReport();
}
