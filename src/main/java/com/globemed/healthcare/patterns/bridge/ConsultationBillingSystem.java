package com.globemed.healthcare.patterns.bridge;

import com.globemed.healthcare.model.Invoice;


public class ConsultationBillingSystem extends BillingSystem {

    public ConsultationBillingSystem(Billing billingImplementor) {
        super(billingImplementor);
    }

    @Override
    public String generateAndProcessBill(Invoice invoice) {
        System.out.println("Generating bill for patient: " + invoice.getPatient().getFirstName());
        System.out.println("Total Amount: $" + invoice.getTotalAmount());
        // The actual payment processing is delegated to the implementor
        return billingImplementor.processPayment(invoice.getTotalAmount());
    }
}
