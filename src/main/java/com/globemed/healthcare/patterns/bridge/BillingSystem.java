package com.globemed.healthcare.patterns.bridge;

import com.globemed.healthcare.model.Invoice;


public abstract class BillingSystem {
    protected Billing billingImplementor;

    protected BillingSystem(Billing billingImplementor) {
        this.billingImplementor = billingImplementor;
    }

    public abstract String generateAndProcessBill(Invoice invoice);
}
