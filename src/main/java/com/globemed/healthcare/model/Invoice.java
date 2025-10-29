package com.globemed.healthcare.model;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String invoiceId;
    private PatientRecord patient;
    private List<BillItem> items;
    private double totalAmount;

    public Invoice(String invoiceId, PatientRecord patient) {
        this.invoiceId = invoiceId;
        this.patient = patient;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public void addItem(BillItem item) {
        this.items.add(item);
        this.totalAmount += item.getCost();
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public PatientRecord getPatient() {
        return patient;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
