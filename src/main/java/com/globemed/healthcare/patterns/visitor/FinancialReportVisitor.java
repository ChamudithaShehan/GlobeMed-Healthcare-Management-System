package com.globemed.healthcare.patterns.visitor;

import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.model.BillItem;
import com.globemed.healthcare.model.BillingInfo;
import com.globemed.healthcare.model.PatientRecord;

public class FinancialReportVisitor implements ReportVisitor {
    private StringBuilder reportContent = new StringBuilder();
    private double totalBilled = 0;

    @Override
    public String visit(PatientRecord patientRecord) {
        reportContent.append("--- Financial Report For Patient ---\n");
        reportContent.append("ID: ").append(patientRecord.getPatientId()).append("\n");
        reportContent.append("Name: ").append(patientRecord.getFirstName()).append(" ").append(patientRecord.getLastName()).append("\n");
        return reportContent.toString();
    }

    @Override
    public String visit(Appointment appointment) {
        return "";
    }

    @Override
    public String visit(BillingInfo billingInfo) {
        reportContent.append("\n--- Invoice ID: ").append(billingInfo.getInvoice().getInvoiceId()).append(" ---\n");
        for(BillItem item : billingInfo.getInvoice().getItems()) {
            reportContent.append(String.format("%-30s $%.2f\n", item.getDescription(), item.getCost()));
        }
        reportContent.append("----------------------------------------\n");
        reportContent.append(String.format("Invoice Total: $%.2f\n", billingInfo.getInvoice().getTotalAmount()));
        totalBilled += billingInfo.getInvoice().getTotalAmount();
        return reportContent.toString();
    }

    @Override
    public String getReport() {
        reportContent.append("\n========================================\n");
        reportContent.append(String.format("TOTAL BILLED AMOUNT FOR PATIENT: $%.2f\n", totalBilled));
        return "********* FINANCIAL REPORT *********\n" + reportContent.toString();
    }
}
