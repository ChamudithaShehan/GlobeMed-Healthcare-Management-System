package com.globemed.healthcare.model;

import com.globemed.healthcare.patterns.visitor.ReportVisitor;
import com.globemed.healthcare.patterns.visitor.Reportable;


public class BillingInfo implements Reportable {
    private Invoice invoice;

    public BillingInfo(Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    @Override
    public String accept(ReportVisitor visitor) {
        return visitor.visit(this);
    }
}
